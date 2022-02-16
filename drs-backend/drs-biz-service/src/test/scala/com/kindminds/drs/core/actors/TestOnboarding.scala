package com.kindminds.drs.core.actors

import java.util
import java.util.Optional

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.kindminds.drs.api.message.command.onboardingApplication._
import com.kindminds.drs.api.message.query.customer.GetReturns
import com.kindminds.drs.api.message.query.onboardingApplication._
import com.kindminds.drs.api.message.query.onboardingApplication.GetTrialList
import com.kindminds.drs.api.message.query.prdocut.dashboard.{GetDailySalesQtyAndRev, GetMTDCampaignSpendAndAcos, GetMTDCustomerCareCases}
import com.kindminds.drs.api.message.query.product.{GetAmazonProductReview, GetProductBaseCode, GetProductSkuCode}
import com.kindminds.drs.core.actors.handlers.command.product._
import com.kindminds.drs.core.actors.handlers.query.product.{OnboardingApplicationLineitemViewHandler, OnboardingApplicationViewHandler, ProductDashboardViewHandler, ProductViewHandler}
import com.kindminds.drs.core.{DrsCmdBus, DrsEventBus, DrsQueryBus}
import com.kindminds.drs.api.v2.biz.domain.model.product.onboarding.OnboardingApplication
import com.kindminds.drs.core.actors.handlers.query.customer.CustomerQueryHandler

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.reflect.ClassTag


class TestOnboarding  extends TestKit(ActorSystem("system"))
  with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll{

  val probe = TestProbe()
  val drsQueryBus = TestActorRef.create(system, Props.create(classOf[DrsQueryBus]), "DrsQueryBus")
  val drsCmdBus = TestActorRef.create(system, Props.create(classOf[DrsCmdBus]), "DrsCmdBus")
  val drsEventBus = new DrsEventBus


  val  applyOnboardingHandler= TestActorRef(ApplyOnboardingHandler.props(drsCmdBus , drsEventBus))
  val  applyOnboardingLineitemHandler=
    TestActorRef(ApplyOnboardingLineitemHandler.props(drsCmdBus , drsEventBus))

  val itemVHandler = TestActorRef(OnboardingApplicationLineitemViewHandler.props(drsQueryBus))

  val pdb = TestActorRef(ProductDashboardViewHandler.props(drsQueryBus))

  val cr = TestActorRef(CustomerQueryHandler.props(drsQueryBus))

  val pv = TestActorRef(ProductViewHandler.props(drsQueryBus))

  "Actor" must {

    within(30000 millis) {

     //pdb ! GetDailySalesQtyAndRev("K510" , None , Some("BP-K510-85U05001R0") , Some("K510-85U05001R0"))

      pdb ! GetDailySalesQtyAndRev(Some("All") , None , None , None)

      //pdb ! GetMTDCustomerCareCases("K510")

     //pdb ! GetMTDCampaignSpendAndAcos("K510" , Some("BP-K510-85U05001R0") , Some("K510-85U05001R0"))

     // pv ! GetProductBaseCode("K510")
     // pv ! GetProductSkuCode("BP-K510-85U17001R0")

      //pv ! GetAmazonProductReview("K510" ,  Some("BP-K510-85U05001R0") , Some("K510-85U05001R0"))

    //  cr ! GetReturns(null,None,None)

     // itemVHandler ! GetTrialList("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")
      //applyOnboardingHandler ! AcceptApplication("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")
      //applyOnboardingHandler ! VerifyServiceFee("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")
      //applyOnboardingHandler ! AcceptServiceFeeQuotation("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")

      //applyOnboardingHandler ! RejectServiceFeeQuotation("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")
     // applyOnboardingHandler ! ConfirmServiceFee("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")

      //applyOnboardingHandler ! ReviewFinalFeasibility("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")

      //applyOnboardingHandler ! RejectProductFeasibility("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")

      //applyOnboardingHandler ! ApproveProductFeasibility("3ef0b2a4-e33c-4f87-b0b2-a4e33c7f8771")


     // applyOnboardingLineitemHandler ! DoTrialList("1d44d4f2-b0f0-4734-84d4-f2b0f0273413" , "{\"ap\":1}" , false )

      //applyOnboardingLineitemHandler ! EvaluateSample("1d44d4f2-b0f0-4734-84d4-f2b0f0273413", "{\"ap\":1}" , false )

      //applyOnboardingLineitemHandler ! PresentSample("1d44d4f2-b0f0-4734-84d4-f2b0f0273413", "{\"ap\":1}" , false )


      //applyOnboardingLineitemHandler ! ProvideComment("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")

      //applyOnboardingLineitemHandler ! GiveFeedback("1d44d4f2-b0f0-4734-84d4-f2b0f0273413" , "{\"ap\":1}" , false )


      //applyOnboardingLineitemHandler ! ApproveSample("1d44d4f2-b0f0-4734-84d4-f2b0f0273413" , "{\"ap\":1}" , false )



      //applyOnboardingLineitemHandler ! RejectCompleteness("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")

      //applyOnboardingLineitemHandler ! CompleteProductDetail("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")


      //applyOnboardingLineitemHandler ! AcceptCompleteness("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")
      //applyOnboardingLineitemHandler ! ConfirmInsurance("1d44d4f2-b0f0-4734-84d4-f2b0f0273413" , "{\"ap\":1}" , false )
      //applyOnboardingLineitemHandler ! CheckInsurance("1d44d4f2-b0f0-4734-84d4-f2b0f0273413", "{\"ap\":1}" , false )

      //applyOnboardingLineitemHandler ! CheckComplianceAndCertificationAvailability("1d44d4f2-b0f0-4734-84d4-f2b0f0273413", "{\"ap\":1}" , false )
      //applyOnboardingLineitemHandler ! CheckProfitability("1d44d4f2-b0f0-4734-84d4-f2b0f0273413", "{\"ap\":1}" , false )
      //applyOnboardingLineitemHandler ! CheckMarketability("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")
      //applyOnboardingLineitemHandler ! CheckReverseLogistics("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")
     // applyOnboardingLineitemHandler ! EvaluatePackageContentAndManual("1d44d4f2-b0f0-4734-84d4-f2b0f0273413")


     //applyOnboardingHandler expectMsgClass(implicitly[ClassTag[Optional[OnboardingApplicationViewQueriesImpl]]].runtimeClass);


    }
  }



}
