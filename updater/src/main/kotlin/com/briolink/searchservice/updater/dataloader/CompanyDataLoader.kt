package com.briolink.searchservice.updater.dataloader

import com.briolink.searchservice.common.dto.location.LocationId
import com.briolink.searchservice.common.jpa.enumeration.LocationTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.searchservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.searchservice.updater.handler.company.CompanyEventData
import com.briolink.searchservice.updater.handler.company.CompanyHandlerService
import com.briolink.searchservice.updater.handler.company.CompanyIndustryData
import com.briolink.searchservice.updater.handler.company.CompanyOccupationData
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID
import kotlin.random.Random

@Component
@Order(1)
class CompanyDataLoader(
    private val companyReadRepository: CompanyReadRepository,
    private val companyHandlerService: CompanyHandlerService
) : DataLoader() {
    val listCompanyName = listOf(
        "PayPal",
        "Nikonv&Sheshuk",
        "Nokia",
        "Amazon",
        "Notion Labs INC",
        "Google",
        "Twitter",
        "GitHub",
        "Walmart",
        "Oaktree Capital",
        "KFC",
        "Tata",
        "Compass Group",
        "Volkswagen",
        "Deutsche Post DHL Group",
    )
    val listDesciption = listOf(
        "PayPal is the safer, easier way to pay and get paid online. The service allows anyone to pay in any way they prefer, including through credit cards, bank accounts, PayPal Smart Connect or account balances, without sharing financial information. PayPal has quickly become a global leader in online payment solutions with more than  million accounts worldwide. Available in 202 countries and 25 currencies around the world, PayPal enables global ecommerce by making payments possible across different locations, currencies, and languages.  PayPal has received more than 20 awards for excellence from the internet industry and the business community - most recently the 2006 Webby Award for Best Financial Services Site and the 2006 Webby People's Voice Award for Best Financial Services Site.  Located in San Jose, California, PayPal was founded in 1998. PayPal (Europe) S.à r.l. et Cie, S.C.A. is a credit institution (or bank) authorised and supervised by Luxembourg’s financial regulator, the Commission de Surveillance du Secteur Financier (or CSSF). CSSF’s registered office: 283, route d’Arlon, L-1150 Luxembourg.", // ktlint-disable max-line-length
        null, null,
        "When Amazon.com launched in 1995, it was with the mission “to be Earth’s most customer-centric company.” What does this mean? It's simple. We're a company that obsesses over customers. Our actions, goals, projects, programmes and inventions begin and end with the customer at the forefront of our minds. In other words, we start with the customer and work backwards. When we hit on something that is really working for customers, we commit to it in the hope that it will turn into an even bigger success. However, it’s not always as straightforward as that. Inventing is messy, and over time, it’s certain that we’ll fail at some big bets too.", // ktlint-disable max-line-length
        "You probably have fifteen tabs open: one for email, one for Slack, one for Google Docs, and on, and on…But have you ever thought about where these work tools came from? Or why there are so many of them?To answer these questions, and to explain why we created Notion, we have to travel back in time.", // ktlint-disable max-line-length
        "American multinational technology company that specializes in Internet-related services and products, which include online advertising technologies, a search engine, cloud computing, software, and hardware. It is considered one of the Big Five companies in the American information technology industry, along with Amazon, Facebook, Apple, and Microsoft. Google was founded on September 4, 1998, by Larry Page and Sergey Brin while they were Ph.D. students at Stanford University in California. Together they own about 14% of its publicly-listed shares and control 56% of the stockholder voting power through super-voting stock. The company went public via an initial public offering (IPO) in 2004. In 2015, Google was reorganized as a wholly-owned subsidiary of Alphabet Inc.. Google is Alphabet's largest subsidiary and is a holding company for Alphabet's Internet properties and interests. Sundar Pichai was appointed CEO of Google on October 24, 2015, replacing Larry Page, who became the CEO of Alphabet. On December 3, 2019, Pichai also became the CEO of Alphabet.", // ktlint-disable max-line-length
        "Twitter is an American microblogging and social networking service on which users post and interact with messages known as tweets. Registered users can post, like, and retweet tweets, but unregistered users can only read those that are publicly available. Users interact with Twitter through browser or mobile frontend software, or programmatically via its APIs. Prior to April 2020 services were accessible via SMS. The service is provided by Twitter, Inc., a corporation based in San Francisco, California, and has more than 25 offices around the world.[14] Tweets were originally restricted to 140 characters, but the limit was doubled to 280 for non-CJK languages in November 2017. Audio and video tweets remain limited to 140 seconds for most accounts.", // ktlint-disable max-line-length
        "GitHub, Inc. is a provider of Internet hosting for software development and version control using Git. It offers the distributed version control and source code management (SCM) functionality of Git, plus its own features. It provides access control and several collaboration features such as bug tracking, feature requests, task management, continuous integration and wikis for every project. Headquartered in California, it has been a subsidiary of Microsoft since 2018.", // ktlint-disable max-line-length
        "Walmart is a retailing company that operates a chain of hypermarkets, discount department stores, and grocery stores.", // ktlint-disable max-line-length
        "Oaktree is a leader among global investment managers specializing in alternative investments.",
        "KFC (also known as Kentucky Fried Chicken) is a global chicken restaurant brand.",
        "Tata Group is an international conglomerate that owns and operates independent companies, with the main focus on steel, hydro-power, hospitality, and airline industries. ", // ktlint-disable max-line-length
        "Compass Group is a global provider of food and support services.",
        "Volkswagen Group (also known as VAG and Volkswagen AG) is an automobile manufacturer. ",
        "Deutsche Post DHL Group (also known as Deutsche Post) is a mail and logistics company.",
    )

    val listLogo = listOf(
        URL("https://www.paypalobjects.com/webstatic/icon/pp258.png"),
        null, null,
        URL("https://regnum.ru/uploads/pictures/news/2018/11/30/regnum_picture_154356037138044_normal.png"),
        URL("https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png"),
        URL("https://w7.pngwing.com/pngs/760/624/png-transparent-google-logo-google-search-advertising-google-company-text-trademark.png"), // ktlint-disable max-line-length
        URL("https://upload.wikimedia.org/wikipedia/ru/thumb/9/9f/Twitter_bird_logo_2012.svg/1261px-Twitter_bird_logo_2012.svg.png"), // ktlint-disable max-line-length
        URL("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"),
        URL("https://logo.clearbit.com/www.walmart.com/"),
        URL("https://logo.clearbit.com/www.oaktreecapital.com/"),
        URL("https://logo.clearbit.com/www.kfc.com/"),
        URL("https://logo.clearbit.com/www.tata.com/"),
        URL("https://logo.clearbit.com/www.compass-group.com/"),
        URL("https://logo.clearbit.com/www.volkswagenag.com/"),
        URL("https://logo.clearbit.com/www.dpdhl.com/"),
    )

    val listWebsite = listOf(
        URL("https://www.paypal.com"),
        null, null,
        URL("https://www.amazon.com/"),
        URL("https://www.notion.so/"),
        URL("https://www.google.com/"),
        URL("https://twitter.com/"),
        URL("https://github.com/"),
        URL("https://www.walmart.com/"),
        URL("https://www.oaktreecapital.com/"),
        URL("https://www.kfc.com/"),
        URL("http://www.tata.com"),
        URL("https://www.compass-group.com/"),
        URL("https://www.volkswagenag.com/"),
        URL("https://www.dpdhl.com/"),
    )

    val listIndustry = listOf(
        CompanyIndustryData(UUID.randomUUID(), "Social"),
        CompanyIndustryData(UUID.randomUUID(), "Internet"),
        CompanyIndustryData(UUID.randomUUID(), "Software"),
        CompanyIndustryData(UUID.randomUUID(), "Trade"),
        CompanyIndustryData(UUID.randomUUID(), "Design"),
        CompanyIndustryData(UUID.randomUUID(), "Beauty"),
        CompanyIndustryData(UUID.randomUUID(), "Music"),
        CompanyIndustryData(UUID.randomUUID(), "Health"),
        CompanyIndustryData(UUID.randomUUID(), "Transport"),
        CompanyIndustryData(UUID.randomUUID(), "Hardware"),
        CompanyIndustryData(UUID.randomUUID(), "Consulting"),
    )

    val listOccupation = listOf(
        CompanyOccupationData(UUID.randomUUID(), "Consulting"),
        CompanyOccupationData(UUID.randomUUID(), "Technology"),
        CompanyOccupationData(UUID.randomUUID(), "Computer Systems Analyst"),
        CompanyOccupationData(UUID.randomUUID(), "Software"),
        CompanyOccupationData(UUID.randomUUID(), "Hardware"),
    )

    override fun loadData() {
        if (companyReadRepository.count().toInt() == 0) {
            val mutableListCompanyRead = mutableListOf<CompanyReadEntity>()
            listCompanyName.forEachIndexed { i, name ->
                companyHandlerService.createOrUpdate(
                    companyEventData = CompanyEventData(
                        id = UUID.randomUUID(),
                        name = name,
                        slug = name.replace(" ", ""),
                        website = listWebsite[i],
                    )
                ).also {
                    mutableListCompanyRead.add(it)
                }
            }

            mutableListCompanyRead.forEachIndexed { index, it ->
                companyHandlerService.createOrUpdate(
                    companyEventData = CompanyEventData(
                        id = it.id,
                        name = it.name + " /update",
                        slug = it.data.slug,
                        logo = listLogo[index],
                        website = it.data.website,
                        description = listDesciption[index],
                        locationId = LocationId(Random.nextInt(1, 120), LocationTypeEnum.fromInt(Random.nextInt(0, 2))),
                        industry = if (Random.nextBoolean()) listIndustry.random() else null,
                        occupation = if (Random.nextBoolean()) listOccupation.random() else null,
                    )
                )
            }
        }
    }
}
