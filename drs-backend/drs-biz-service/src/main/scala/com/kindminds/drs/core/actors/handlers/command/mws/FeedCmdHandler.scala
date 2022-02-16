package com.kindminds.drs.core.actors.handlers.command.mws

import java.math.BigInteger

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.adapter.AmazonMwsFeedAdapter
import com.kindminds.drs.api.message.command.amazon.mws.feeds.{RequestFeedSubmissionList, RequestFeedSubmissionListByCount, RequestFeedSubmissionResult, SubmitFeed}
import com.kindminds.drs.biz.service.util.BizCoreCtx
import com.kindminds.drs.core.RegisterCommandHandler
import com.kindminds.drs.mws.xml.Product.DescriptionData
import com.kindminds.drs.mws.xml.{AmazonEnvelope, BaseCurrencyCode, CurrencyAmount, Header, Health, HealthMisc, Product, StandardProductID}



object FeedsHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new FeedsHandler(drsCmdBus))

}

// val fh = system.actorOf(FeedsHandler.props(cmdBus), "feedsHandler")
//    fh ! RequestFeedSubmissionResult(Marketplace.AMAZON_COM, "243695018570")
//    val feedTypeList = new TypeList().withType(MwsFeedType.Flat_File_Listings_Feed.getValue)
//    val idList = new IdList()
//    idList.getId.add("243695018570")
//    fh ! RequestFeedSubmissionList(Marketplace.AMAZON_COM, idList)
//    fh ! SubmitFeed("C:/Users/HyperionFive/Desktop/user/TrivetsXMLTest.xml", Marketplace.AMAZON_COM, MwsFeedType.Product_Feed)
//    fh ! RequestFeedSubmissionCount(Marketplace.AMAZON_COM, feedTypeList)

class FeedsHandler(drsCmdBus: ActorRef) extends Actor with ActorLogging {

  val name = self.path.name

  val springCtx = BizCoreCtx.get

  drsCmdBus ! RegisterCommandHandler(name,classOf[SubmitFeed].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestFeedSubmissionList].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestFeedSubmissionListByCount].getName ,self)
  //  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestFeedSubmissionCount].getName ,self)
  //  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestCancelFeedSubmissions].getName ,self)
  drsCmdBus ! RegisterCommandHandler(name,classOf[RequestFeedSubmissionResult].getName ,self)


  val mws = springCtx.getBean(classOf[AmazonMwsFeedAdapter]).asInstanceOf[AmazonMwsFeedAdapter]

  //  import context.dispatcher

  def receive = {

    case sf: SubmitFeed =>

      //println("AAAA")

      val ae = new AmazonEnvelope

      val header = new Header
      header.setMerchantIdentifier("A3QY80NORGXT27")
      header.setDocumentVersion("1.01")

      ae.setHeader(header)
      ae.setMessageType("Product")
      ae.setPurgeAndReplace(false)

      val aeMsg= new AmazonEnvelope.Message
      aeMsg.setMessageID( BigInteger.ONE)
      aeMsg.setOperationType("Update")


/*
 <Message>
        <MessageID>1</MessageID>
        <OperationType>Update</OperationType>
        <Product>
            <SKU>5T1-Child7</SKU>
            <StandardProductID>
                <Type>EAN</Type>
                <Value>4712805507283</Value>
            </StandardProductID>
            <DescriptionData>
                <Title>Silicone Trivet Mat Black</Title>
                <Brand>MOCHI MOCHI</Brand>
                <Description>Silicone Trivet Mat</Description>
                <BulletPoint>Example Bullet Point 1</BulletPoint>
                <BulletPoint>Example Bullet Point 2</BulletPoint>
                <MSRP currency="USD">25.19</MSRP>
                <Manufacturer>Example Product Manufacturer</Manufacturer>
                <ItemType>trivets</ItemType>

				<UnitCount>1</UnitCount>
            </DescriptionData>
            <ProductData>
                <Health>
                    <ProductType>
                        <HealthMisc>
                            <Ingredients>Example Ingredients</Ingredients>
                            <Directions>Example Directions</Directions>
                        </HealthMisc>
                    </ProductType>
                </Health>
            </ProductData>
        </Product>
    </Message>
 */

      val p = new Product
      p.setSKU("DCP-SS-test")
      val stdPId = new StandardProductID
      stdPId.setType("EAN")
      stdPId.setValue("471280550077")
      p.setStandardProductID(stdPId)

      val dd = new DescriptionData
      dd.setTitle("Inflatable Travel Pillow Set version 2")
      dd.setBrand("KMI")
      dd.setDescription("User can arrange the pillow set to the most comfortable position.")

      //the problem is unit count
      //dd.setUnitCount(java.math.BigDecimal.valueOf(0.00))
      //dd.setPPUCountType("")


      val msrp = new CurrencyAmount
      msrp.setCurrency(BaseCurrencyCode.USD)
      msrp.setValue(java.math.BigDecimal.valueOf(25.19))
      dd.setMSRP(msrp)

      dd.setManufacturer("Example Product Manufacturer 22")
      dd.setItemType("trivets 22")

     val bpList =  dd.getBulletPoint()
      bpList.add("Example Bullet Point 1")
      bpList.add("Example Bullet Point 2")

      val pd = new Product.ProductData
      val h = new Health
      val hpt = new Health.ProductType

      val hm = new HealthMisc
      hm.getIngredients.add("Example Ingredients")
      hm.setDirections("Example Directions")

      hpt.setHealthMisc(hm)
      h.setProductType(hpt)
      pd.setHealth(h)

      //todo arthur when we add descriptiondata , we can't successfullly submit.
      p.setDescriptionData(dd)
      p.setProductData(pd)


      aeMsg.setProduct(p)
      ae.getMessage.add(aeMsg)


      import javax.xml.bind.JAXBContext
      import javax.xml.bind.JAXBException
      import javax.xml.bind.Marshaller
      import java.io.StringWriter
      try { //Create JAXB Context
        val jaxbContext = JAXBContext.newInstance(classOf[AmazonEnvelope])
        //Create Marshaller
        val jaxbMarshaller = jaxbContext.createMarshaller
        //Required formatting??
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        //Print XML String to Console
        val sw = new StringWriter
        //Write XML to StringWriter
        jaxbMarshaller.marshal(ae, sw)
        //Verify XML Content
        val xmlContent = sw.toString
        //System.out.//println(xmlContent)

        mws.submitFeed(xmlContent, sf.marketplace, sf.feedType)

      } catch {
        case e: JAXBException =>
          e.printStackTrace()
      }


    case r: RequestFeedSubmissionList =>
      //println("RequestFeedSubmissionList")
      mws.getFeedSubmissionList(r.marketplace, r.feedSubmissionIdList)

    case rbc: RequestFeedSubmissionListByCount =>
      //println("RequestFeedSubmissionListByCount")

      mws.getFeedSubmissionList(
        rbc.marketplace, rbc.maxCount, rbc.feedTypeList);

    //    case fc: RequestFeedSubmissionCount=>
    //      mws.getFeedSubmissionCount(fc.marketplace, fc.feedTypeList)
    //
    //    case cs: RequestCancelFeedSubmissions =>
    //      mws.cancelFeedSubmissions(cs.marketplace, cs.feedIdList)

    case fr: RequestFeedSubmissionResult =>
      //println("RequestFeedSubmissionResult")
      mws.getFeedSubmissionResult(fr.marketplace, fr.feedSubmissionId)




  }





}
