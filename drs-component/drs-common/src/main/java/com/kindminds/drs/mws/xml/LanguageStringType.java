//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LanguageStringType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LanguageStringType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Abkhazian"/&gt;
 *     &lt;enumeration value="Adygei"/&gt;
 *     &lt;enumeration value="Afar"/&gt;
 *     &lt;enumeration value="Afrikaans"/&gt;
 *     &lt;enumeration value="Albanian"/&gt;
 *     &lt;enumeration value="Alsatian"/&gt;
 *     &lt;enumeration value="Amharic"/&gt;
 *     &lt;enumeration value="Arabic"/&gt;
 *     &lt;enumeration value="Aramaic"/&gt;
 *     &lt;enumeration value="Armenian"/&gt;
 *     &lt;enumeration value="Assamese"/&gt;
 *     &lt;enumeration value="Aymara"/&gt;
 *     &lt;enumeration value="Azerbaijani"/&gt;
 *     &lt;enumeration value="Bambara"/&gt;
 *     &lt;enumeration value="Bashkir"/&gt;
 *     &lt;enumeration value="Basque"/&gt;
 *     &lt;enumeration value="Bengali"/&gt;
 *     &lt;enumeration value="Berber"/&gt;
 *     &lt;enumeration value="Bhutani"/&gt;
 *     &lt;enumeration value="Bihari"/&gt;
 *     &lt;enumeration value="Bislama"/&gt;
 *     &lt;enumeration value="Breton"/&gt;
 *     &lt;enumeration value="Bulgarian"/&gt;
 *     &lt;enumeration value="Burmese"/&gt;
 *     &lt;enumeration value="Buryat"/&gt;
 *     &lt;enumeration value="Byelorussian"/&gt;
 *     &lt;enumeration value="CantoneseChinese"/&gt;
 *     &lt;enumeration value="Castillian"/&gt;
 *     &lt;enumeration value="Catalan"/&gt;
 *     &lt;enumeration value="Cayuga"/&gt;
 *     &lt;enumeration value="Cheyenne"/&gt;
 *     &lt;enumeration value="Chinese"/&gt;
 *     &lt;enumeration value="ClassicalNewari"/&gt;
 *     &lt;enumeration value="Cornish"/&gt;
 *     &lt;enumeration value="Corsican"/&gt;
 *     &lt;enumeration value="Creole"/&gt;
 *     &lt;enumeration value="CrimeanTatar"/&gt;
 *     &lt;enumeration value="Croatian"/&gt;
 *     &lt;enumeration value="Czech"/&gt;
 *     &lt;enumeration value="Danish"/&gt;
 *     &lt;enumeration value="Dargwa"/&gt;
 *     &lt;enumeration value="Dutch"/&gt;
 *     &lt;enumeration value="English"/&gt;
 *     &lt;enumeration value="Esperanto"/&gt;
 *     &lt;enumeration value="Estonian"/&gt;
 *     &lt;enumeration value="Faroese"/&gt;
 *     &lt;enumeration value="Farsi"/&gt;
 *     &lt;enumeration value="Fiji"/&gt;
 *     &lt;enumeration value="Filipino"/&gt;
 *     &lt;enumeration value="Finnish"/&gt;
 *     &lt;enumeration value="Flemish"/&gt;
 *     &lt;enumeration value="French"/&gt;
 *     &lt;enumeration value="FrenchCanadian"/&gt;
 *     &lt;enumeration value="Frisian"/&gt;
 *     &lt;enumeration value="Galician"/&gt;
 *     &lt;enumeration value="Georgian"/&gt;
 *     &lt;enumeration value="German"/&gt;
 *     &lt;enumeration value="Gibberish"/&gt;
 *     &lt;enumeration value="Greek"/&gt;
 *     &lt;enumeration value="Greenlandic"/&gt;
 *     &lt;enumeration value="Guarani"/&gt;
 *     &lt;enumeration value="Gujarati"/&gt;
 *     &lt;enumeration value="Gullah"/&gt;
 *     &lt;enumeration value="Hausa"/&gt;
 *     &lt;enumeration value="Hawaiian"/&gt;
 *     &lt;enumeration value="Hebrew"/&gt;
 *     &lt;enumeration value="Hindi"/&gt;
 *     &lt;enumeration value="Hmong"/&gt;
 *     &lt;enumeration value="Hungarian"/&gt;
 *     &lt;enumeration value="Icelandic"/&gt;
 *     &lt;enumeration value="IndoEuropean"/&gt;
 *     &lt;enumeration value="Indonesian"/&gt;
 *     &lt;enumeration value="Ingush"/&gt;
 *     &lt;enumeration value="Interlingua"/&gt;
 *     &lt;enumeration value="Interlingue"/&gt;
 *     &lt;enumeration value="Inuktitun"/&gt;
 *     &lt;enumeration value="Inuktitut"/&gt;
 *     &lt;enumeration value="Inupiak"/&gt;
 *     &lt;enumeration value="Inupiaq"/&gt;
 *     &lt;enumeration value="Irish"/&gt;
 *     &lt;enumeration value="Italian"/&gt;
 *     &lt;enumeration value="Japanese"/&gt;
 *     &lt;enumeration value="Javanese"/&gt;
 *     &lt;enumeration value="Kalaallisut"/&gt;
 *     &lt;enumeration value="Kalmyk"/&gt;
 *     &lt;enumeration value="Kannada"/&gt;
 *     &lt;enumeration value="KarachayBalkar"/&gt;
 *     &lt;enumeration value="Kashmiri"/&gt;
 *     &lt;enumeration value="Kashubian"/&gt;
 *     &lt;enumeration value="Kazakh"/&gt;
 *     &lt;enumeration value="Khmer"/&gt;
 *     &lt;enumeration value="Kinyarwanda"/&gt;
 *     &lt;enumeration value="Kirghiz"/&gt;
 *     &lt;enumeration value="Kirundi"/&gt;
 *     &lt;enumeration value="Klingon"/&gt;
 *     &lt;enumeration value="Korean"/&gt;
 *     &lt;enumeration value="Kurdish"/&gt;
 *     &lt;enumeration value="Ladino"/&gt;
 *     &lt;enumeration value="Lao"/&gt;
 *     &lt;enumeration value="Lapp"/&gt;
 *     &lt;enumeration value="Latin"/&gt;
 *     &lt;enumeration value="Latvian"/&gt;
 *     &lt;enumeration value="Lingala"/&gt;
 *     &lt;enumeration value="Lithuanian"/&gt;
 *     &lt;enumeration value="Lojban"/&gt;
 *     &lt;enumeration value="LowerSorbian"/&gt;
 *     &lt;enumeration value="Macedonian"/&gt;
 *     &lt;enumeration value="Malagasy"/&gt;
 *     &lt;enumeration value="Malay"/&gt;
 *     &lt;enumeration value="Malayalam"/&gt;
 *     &lt;enumeration value="Maltese"/&gt;
 *     &lt;enumeration value="MandarinChinese"/&gt;
 *     &lt;enumeration value="Maori"/&gt;
 *     &lt;enumeration value="Marathi"/&gt;
 *     &lt;enumeration value="Mende"/&gt;
 *     &lt;enumeration value="MiddleEnglish"/&gt;
 *     &lt;enumeration value="Mirandese"/&gt;
 *     &lt;enumeration value="Moksha"/&gt;
 *     &lt;enumeration value="Moldavian"/&gt;
 *     &lt;enumeration value="Mongo"/&gt;
 *     &lt;enumeration value="Mongolian"/&gt;
 *     &lt;enumeration value="Multilingual"/&gt;
 *     &lt;enumeration value="Nauru"/&gt;
 *     &lt;enumeration value="Navaho"/&gt;
 *     &lt;enumeration value="Nepali"/&gt;
 *     &lt;enumeration value="Nogai"/&gt;
 *     &lt;enumeration value="Norwegian"/&gt;
 *     &lt;enumeration value="Occitan"/&gt;
 *     &lt;enumeration value="OldEnglish"/&gt;
 *     &lt;enumeration value="Oriya"/&gt;
 *     &lt;enumeration value="Oromo"/&gt;
 *     &lt;enumeration value="Pashto"/&gt;
 *     &lt;enumeration value="Persian"/&gt;
 *     &lt;enumeration value="PigLatin"/&gt;
 *     &lt;enumeration value="Polish"/&gt;
 *     &lt;enumeration value="Portuguese"/&gt;
 *     &lt;enumeration value="Punjabi"/&gt;
 *     &lt;enumeration value="Quechua"/&gt;
 *     &lt;enumeration value="Romance"/&gt;
 *     &lt;enumeration value="Romanian"/&gt;
 *     &lt;enumeration value="Romany"/&gt;
 *     &lt;enumeration value="Russian"/&gt;
 *     &lt;enumeration value="Samaritan"/&gt;
 *     &lt;enumeration value="Samoan"/&gt;
 *     &lt;enumeration value="Sangho"/&gt;
 *     &lt;enumeration value="Sanskrit"/&gt;
 *     &lt;enumeration value="Serbian"/&gt;
 *     &lt;enumeration value="Serbo-Croatian"/&gt;
 *     &lt;enumeration value="Sesotho"/&gt;
 *     &lt;enumeration value="Setswana"/&gt;
 *     &lt;enumeration value="Shona"/&gt;
 *     &lt;enumeration value="SichuanYi"/&gt;
 *     &lt;enumeration value="Sicilian"/&gt;
 *     &lt;enumeration value="SignLanguage"/&gt;
 *     &lt;enumeration value="Sindhi"/&gt;
 *     &lt;enumeration value="Sinhalese"/&gt;
 *     &lt;enumeration value="Siswati"/&gt;
 *     &lt;enumeration value="Slavic"/&gt;
 *     &lt;enumeration value="Slovak"/&gt;
 *     &lt;enumeration value="Slovakian"/&gt;
 *     &lt;enumeration value="Slovene"/&gt;
 *     &lt;enumeration value="Somali"/&gt;
 *     &lt;enumeration value="Spanish"/&gt;
 *     &lt;enumeration value="Sumerian"/&gt;
 *     &lt;enumeration value="Sundanese"/&gt;
 *     &lt;enumeration value="Swahili"/&gt;
 *     &lt;enumeration value="Swedish"/&gt;
 *     &lt;enumeration value="SwissGerman"/&gt;
 *     &lt;enumeration value="Syriac"/&gt;
 *     &lt;enumeration value="Tagalog"/&gt;
 *     &lt;enumeration value="TaiwaneseChinese"/&gt;
 *     &lt;enumeration value="Tajik"/&gt;
 *     &lt;enumeration value="Tamil"/&gt;
 *     &lt;enumeration value="Tatar"/&gt;
 *     &lt;enumeration value="Telugu"/&gt;
 *     &lt;enumeration value="Thai"/&gt;
 *     &lt;enumeration value="Tibetan"/&gt;
 *     &lt;enumeration value="Tigrinya"/&gt;
 *     &lt;enumeration value="Tonga"/&gt;
 *     &lt;enumeration value="Tsonga"/&gt;
 *     &lt;enumeration value="Turkish"/&gt;
 *     &lt;enumeration value="Turkmen"/&gt;
 *     &lt;enumeration value="Twi"/&gt;
 *     &lt;enumeration value="Udmurt"/&gt;
 *     &lt;enumeration value="Uighur"/&gt;
 *     &lt;enumeration value="Ukrainian"/&gt;
 *     &lt;enumeration value="Ukranian"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Urdu"/&gt;
 *     &lt;enumeration value="Uzbek"/&gt;
 *     &lt;enumeration value="Vietnamese"/&gt;
 *     &lt;enumeration value="Volapuk"/&gt;
 *     &lt;enumeration value="Welsh"/&gt;
 *     &lt;enumeration value="Wolof"/&gt;
 *     &lt;enumeration value="Xhosa"/&gt;
 *     &lt;enumeration value="Yiddish"/&gt;
 *     &lt;enumeration value="Yoruba"/&gt;
 *     &lt;enumeration value="Zhuang"/&gt;
 *     &lt;enumeration value="Zulu"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LanguageStringType")
@XmlEnum
public enum LanguageStringType {

    @XmlEnumValue("Abkhazian")
    ABKHAZIAN("Abkhazian"),
    @XmlEnumValue("Adygei")
    ADYGEI("Adygei"),
    @XmlEnumValue("Afar")
    AFAR("Afar"),
    @XmlEnumValue("Afrikaans")
    AFRIKAANS("Afrikaans"),
    @XmlEnumValue("Albanian")
    ALBANIAN("Albanian"),
    @XmlEnumValue("Alsatian")
    ALSATIAN("Alsatian"),
    @XmlEnumValue("Amharic")
    AMHARIC("Amharic"),
    @XmlEnumValue("Arabic")
    ARABIC("Arabic"),
    @XmlEnumValue("Aramaic")
    ARAMAIC("Aramaic"),
    @XmlEnumValue("Armenian")
    ARMENIAN("Armenian"),
    @XmlEnumValue("Assamese")
    ASSAMESE("Assamese"),
    @XmlEnumValue("Aymara")
    AYMARA("Aymara"),
    @XmlEnumValue("Azerbaijani")
    AZERBAIJANI("Azerbaijani"),
    @XmlEnumValue("Bambara")
    BAMBARA("Bambara"),
    @XmlEnumValue("Bashkir")
    BASHKIR("Bashkir"),
    @XmlEnumValue("Basque")
    BASQUE("Basque"),
    @XmlEnumValue("Bengali")
    BENGALI("Bengali"),
    @XmlEnumValue("Berber")
    BERBER("Berber"),
    @XmlEnumValue("Bhutani")
    BHUTANI("Bhutani"),
    @XmlEnumValue("Bihari")
    BIHARI("Bihari"),
    @XmlEnumValue("Bislama")
    BISLAMA("Bislama"),
    @XmlEnumValue("Breton")
    BRETON("Breton"),
    @XmlEnumValue("Bulgarian")
    BULGARIAN("Bulgarian"),
    @XmlEnumValue("Burmese")
    BURMESE("Burmese"),
    @XmlEnumValue("Buryat")
    BURYAT("Buryat"),
    @XmlEnumValue("Byelorussian")
    BYELORUSSIAN("Byelorussian"),
    @XmlEnumValue("CantoneseChinese")
    CANTONESE_CHINESE("CantoneseChinese"),
    @XmlEnumValue("Castillian")
    CASTILLIAN("Castillian"),
    @XmlEnumValue("Catalan")
    CATALAN("Catalan"),
    @XmlEnumValue("Cayuga")
    CAYUGA("Cayuga"),
    @XmlEnumValue("Cheyenne")
    CHEYENNE("Cheyenne"),
    @XmlEnumValue("Chinese")
    CHINESE("Chinese"),
    @XmlEnumValue("ClassicalNewari")
    CLASSICAL_NEWARI("ClassicalNewari"),
    @XmlEnumValue("Cornish")
    CORNISH("Cornish"),
    @XmlEnumValue("Corsican")
    CORSICAN("Corsican"),
    @XmlEnumValue("Creole")
    CREOLE("Creole"),
    @XmlEnumValue("CrimeanTatar")
    CRIMEAN_TATAR("CrimeanTatar"),
    @XmlEnumValue("Croatian")
    CROATIAN("Croatian"),
    @XmlEnumValue("Czech")
    CZECH("Czech"),
    @XmlEnumValue("Danish")
    DANISH("Danish"),
    @XmlEnumValue("Dargwa")
    DARGWA("Dargwa"),
    @XmlEnumValue("Dutch")
    DUTCH("Dutch"),
    @XmlEnumValue("English")
    ENGLISH("English"),
    @XmlEnumValue("Esperanto")
    ESPERANTO("Esperanto"),
    @XmlEnumValue("Estonian")
    ESTONIAN("Estonian"),
    @XmlEnumValue("Faroese")
    FAROESE("Faroese"),
    @XmlEnumValue("Farsi")
    FARSI("Farsi"),
    @XmlEnumValue("Fiji")
    FIJI("Fiji"),
    @XmlEnumValue("Filipino")
    FILIPINO("Filipino"),
    @XmlEnumValue("Finnish")
    FINNISH("Finnish"),
    @XmlEnumValue("Flemish")
    FLEMISH("Flemish"),
    @XmlEnumValue("French")
    FRENCH("French"),
    @XmlEnumValue("FrenchCanadian")
    FRENCH_CANADIAN("FrenchCanadian"),
    @XmlEnumValue("Frisian")
    FRISIAN("Frisian"),
    @XmlEnumValue("Galician")
    GALICIAN("Galician"),
    @XmlEnumValue("Georgian")
    GEORGIAN("Georgian"),
    @XmlEnumValue("German")
    GERMAN("German"),
    @XmlEnumValue("Gibberish")
    GIBBERISH("Gibberish"),
    @XmlEnumValue("Greek")
    GREEK("Greek"),
    @XmlEnumValue("Greenlandic")
    GREENLANDIC("Greenlandic"),
    @XmlEnumValue("Guarani")
    GUARANI("Guarani"),
    @XmlEnumValue("Gujarati")
    GUJARATI("Gujarati"),
    @XmlEnumValue("Gullah")
    GULLAH("Gullah"),
    @XmlEnumValue("Hausa")
    HAUSA("Hausa"),
    @XmlEnumValue("Hawaiian")
    HAWAIIAN("Hawaiian"),
    @XmlEnumValue("Hebrew")
    HEBREW("Hebrew"),
    @XmlEnumValue("Hindi")
    HINDI("Hindi"),
    @XmlEnumValue("Hmong")
    HMONG("Hmong"),
    @XmlEnumValue("Hungarian")
    HUNGARIAN("Hungarian"),
    @XmlEnumValue("Icelandic")
    ICELANDIC("Icelandic"),
    @XmlEnumValue("IndoEuropean")
    INDO_EUROPEAN("IndoEuropean"),
    @XmlEnumValue("Indonesian")
    INDONESIAN("Indonesian"),
    @XmlEnumValue("Ingush")
    INGUSH("Ingush"),
    @XmlEnumValue("Interlingua")
    INTERLINGUA("Interlingua"),
    @XmlEnumValue("Interlingue")
    INTERLINGUE("Interlingue"),
    @XmlEnumValue("Inuktitun")
    INUKTITUN("Inuktitun"),
    @XmlEnumValue("Inuktitut")
    INUKTITUT("Inuktitut"),
    @XmlEnumValue("Inupiak")
    INUPIAK("Inupiak"),
    @XmlEnumValue("Inupiaq")
    INUPIAQ("Inupiaq"),
    @XmlEnumValue("Irish")
    IRISH("Irish"),
    @XmlEnumValue("Italian")
    ITALIAN("Italian"),
    @XmlEnumValue("Japanese")
    JAPANESE("Japanese"),
    @XmlEnumValue("Javanese")
    JAVANESE("Javanese"),
    @XmlEnumValue("Kalaallisut")
    KALAALLISUT("Kalaallisut"),
    @XmlEnumValue("Kalmyk")
    KALMYK("Kalmyk"),
    @XmlEnumValue("Kannada")
    KANNADA("Kannada"),
    @XmlEnumValue("KarachayBalkar")
    KARACHAY_BALKAR("KarachayBalkar"),
    @XmlEnumValue("Kashmiri")
    KASHMIRI("Kashmiri"),
    @XmlEnumValue("Kashubian")
    KASHUBIAN("Kashubian"),
    @XmlEnumValue("Kazakh")
    KAZAKH("Kazakh"),
    @XmlEnumValue("Khmer")
    KHMER("Khmer"),
    @XmlEnumValue("Kinyarwanda")
    KINYARWANDA("Kinyarwanda"),
    @XmlEnumValue("Kirghiz")
    KIRGHIZ("Kirghiz"),
    @XmlEnumValue("Kirundi")
    KIRUNDI("Kirundi"),
    @XmlEnumValue("Klingon")
    KLINGON("Klingon"),
    @XmlEnumValue("Korean")
    KOREAN("Korean"),
    @XmlEnumValue("Kurdish")
    KURDISH("Kurdish"),
    @XmlEnumValue("Ladino")
    LADINO("Ladino"),
    @XmlEnumValue("Lao")
    LAO("Lao"),
    @XmlEnumValue("Lapp")
    LAPP("Lapp"),
    @XmlEnumValue("Latin")
    LATIN("Latin"),
    @XmlEnumValue("Latvian")
    LATVIAN("Latvian"),
    @XmlEnumValue("Lingala")
    LINGALA("Lingala"),
    @XmlEnumValue("Lithuanian")
    LITHUANIAN("Lithuanian"),
    @XmlEnumValue("Lojban")
    LOJBAN("Lojban"),
    @XmlEnumValue("LowerSorbian")
    LOWER_SORBIAN("LowerSorbian"),
    @XmlEnumValue("Macedonian")
    MACEDONIAN("Macedonian"),
    @XmlEnumValue("Malagasy")
    MALAGASY("Malagasy"),
    @XmlEnumValue("Malay")
    MALAY("Malay"),
    @XmlEnumValue("Malayalam")
    MALAYALAM("Malayalam"),
    @XmlEnumValue("Maltese")
    MALTESE("Maltese"),
    @XmlEnumValue("MandarinChinese")
    MANDARIN_CHINESE("MandarinChinese"),
    @XmlEnumValue("Maori")
    MAORI("Maori"),
    @XmlEnumValue("Marathi")
    MARATHI("Marathi"),
    @XmlEnumValue("Mende")
    MENDE("Mende"),
    @XmlEnumValue("MiddleEnglish")
    MIDDLE_ENGLISH("MiddleEnglish"),
    @XmlEnumValue("Mirandese")
    MIRANDESE("Mirandese"),
    @XmlEnumValue("Moksha")
    MOKSHA("Moksha"),
    @XmlEnumValue("Moldavian")
    MOLDAVIAN("Moldavian"),
    @XmlEnumValue("Mongo")
    MONGO("Mongo"),
    @XmlEnumValue("Mongolian")
    MONGOLIAN("Mongolian"),
    @XmlEnumValue("Multilingual")
    MULTILINGUAL("Multilingual"),
    @XmlEnumValue("Nauru")
    NAURU("Nauru"),
    @XmlEnumValue("Navaho")
    NAVAHO("Navaho"),
    @XmlEnumValue("Nepali")
    NEPALI("Nepali"),
    @XmlEnumValue("Nogai")
    NOGAI("Nogai"),
    @XmlEnumValue("Norwegian")
    NORWEGIAN("Norwegian"),
    @XmlEnumValue("Occitan")
    OCCITAN("Occitan"),
    @XmlEnumValue("OldEnglish")
    OLD_ENGLISH("OldEnglish"),
    @XmlEnumValue("Oriya")
    ORIYA("Oriya"),
    @XmlEnumValue("Oromo")
    OROMO("Oromo"),
    @XmlEnumValue("Pashto")
    PASHTO("Pashto"),
    @XmlEnumValue("Persian")
    PERSIAN("Persian"),
    @XmlEnumValue("PigLatin")
    PIG_LATIN("PigLatin"),
    @XmlEnumValue("Polish")
    POLISH("Polish"),
    @XmlEnumValue("Portuguese")
    PORTUGUESE("Portuguese"),
    @XmlEnumValue("Punjabi")
    PUNJABI("Punjabi"),
    @XmlEnumValue("Quechua")
    QUECHUA("Quechua"),
    @XmlEnumValue("Romance")
    ROMANCE("Romance"),
    @XmlEnumValue("Romanian")
    ROMANIAN("Romanian"),
    @XmlEnumValue("Romany")
    ROMANY("Romany"),
    @XmlEnumValue("Russian")
    RUSSIAN("Russian"),
    @XmlEnumValue("Samaritan")
    SAMARITAN("Samaritan"),
    @XmlEnumValue("Samoan")
    SAMOAN("Samoan"),
    @XmlEnumValue("Sangho")
    SANGHO("Sangho"),
    @XmlEnumValue("Sanskrit")
    SANSKRIT("Sanskrit"),
    @XmlEnumValue("Serbian")
    SERBIAN("Serbian"),
    @XmlEnumValue("Serbo-Croatian")
    SERBO___CROATIAN("Serbo-Croatian"),
    @XmlEnumValue("Sesotho")
    SESOTHO("Sesotho"),
    @XmlEnumValue("Setswana")
    SETSWANA("Setswana"),
    @XmlEnumValue("Shona")
    SHONA("Shona"),
    @XmlEnumValue("SichuanYi")
    SICHUAN_YI("SichuanYi"),
    @XmlEnumValue("Sicilian")
    SICILIAN("Sicilian"),
    @XmlEnumValue("SignLanguage")
    SIGN_LANGUAGE("SignLanguage"),
    @XmlEnumValue("Sindhi")
    SINDHI("Sindhi"),
    @XmlEnumValue("Sinhalese")
    SINHALESE("Sinhalese"),
    @XmlEnumValue("Siswati")
    SISWATI("Siswati"),
    @XmlEnumValue("Slavic")
    SLAVIC("Slavic"),
    @XmlEnumValue("Slovak")
    SLOVAK("Slovak"),
    @XmlEnumValue("Slovakian")
    SLOVAKIAN("Slovakian"),
    @XmlEnumValue("Slovene")
    SLOVENE("Slovene"),
    @XmlEnumValue("Somali")
    SOMALI("Somali"),
    @XmlEnumValue("Spanish")
    SPANISH("Spanish"),
    @XmlEnumValue("Sumerian")
    SUMERIAN("Sumerian"),
    @XmlEnumValue("Sundanese")
    SUNDANESE("Sundanese"),
    @XmlEnumValue("Swahili")
    SWAHILI("Swahili"),
    @XmlEnumValue("Swedish")
    SWEDISH("Swedish"),
    @XmlEnumValue("SwissGerman")
    SWISS_GERMAN("SwissGerman"),
    @XmlEnumValue("Syriac")
    SYRIAC("Syriac"),
    @XmlEnumValue("Tagalog")
    TAGALOG("Tagalog"),
    @XmlEnumValue("TaiwaneseChinese")
    TAIWANESE_CHINESE("TaiwaneseChinese"),
    @XmlEnumValue("Tajik")
    TAJIK("Tajik"),
    @XmlEnumValue("Tamil")
    TAMIL("Tamil"),
    @XmlEnumValue("Tatar")
    TATAR("Tatar"),
    @XmlEnumValue("Telugu")
    TELUGU("Telugu"),
    @XmlEnumValue("Thai")
    THAI("Thai"),
    @XmlEnumValue("Tibetan")
    TIBETAN("Tibetan"),
    @XmlEnumValue("Tigrinya")
    TIGRINYA("Tigrinya"),
    @XmlEnumValue("Tonga")
    TONGA("Tonga"),
    @XmlEnumValue("Tsonga")
    TSONGA("Tsonga"),
    @XmlEnumValue("Turkish")
    TURKISH("Turkish"),
    @XmlEnumValue("Turkmen")
    TURKMEN("Turkmen"),
    @XmlEnumValue("Twi")
    TWI("Twi"),
    @XmlEnumValue("Udmurt")
    UDMURT("Udmurt"),
    @XmlEnumValue("Uighur")
    UIGHUR("Uighur"),
    @XmlEnumValue("Ukrainian")
    UKRAINIAN("Ukrainian"),
    @XmlEnumValue("Ukranian")
    UKRANIAN("Ukranian"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Urdu")
    URDU("Urdu"),
    @XmlEnumValue("Uzbek")
    UZBEK("Uzbek"),
    @XmlEnumValue("Vietnamese")
    VIETNAMESE("Vietnamese"),
    @XmlEnumValue("Volapuk")
    VOLAPUK("Volapuk"),
    @XmlEnumValue("Welsh")
    WELSH("Welsh"),
    @XmlEnumValue("Wolof")
    WOLOF("Wolof"),
    @XmlEnumValue("Xhosa")
    XHOSA("Xhosa"),
    @XmlEnumValue("Yiddish")
    YIDDISH("Yiddish"),
    @XmlEnumValue("Yoruba")
    YORUBA("Yoruba"),
    @XmlEnumValue("Zhuang")
    ZHUANG("Zhuang"),
    @XmlEnumValue("Zulu")
    ZULU("Zulu");
    private final String value;

    LanguageStringType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LanguageStringType fromValue(String v) {
        for (LanguageStringType c: LanguageStringType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
