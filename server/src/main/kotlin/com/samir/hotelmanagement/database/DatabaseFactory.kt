package com.samir.hotelmanagement.database

import com.samir.core.Hotel
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users, Hotels, Rooms, Bookings)
            //seedData()
        }
    }

    private fun seedData() {
        if (Hotels.selectAll().empty()) {
            val hotelsList = getDummyHotels()
            hotelsList.forEach { hotel ->
                val hiltonInsertId = Hotels.insert {
                    it[name] = hotel.name
                    it[location] = hotel.location
                    it[description] = hotel.description
                    it[imageUrl] = hotel.imageUrl
                } get Hotels.id

                Rooms.insert {
                    it[hotelId] = hiltonInsertId
                    it[roomNumber] = "100"
                    it[type] = "Standard Room"
                    it[pricePerNight] = 170.0
                }

                Rooms.insert {
                    it[hotelId] = hiltonInsertId
                    it[roomNumber] = "101"
                    it[type] = "Deluxe Room"
                    it[pricePerNight] = 250.0
                }

                Rooms.insert {
                    it[hotelId] = hiltonInsertId
                    it[roomNumber] = "102"
                    it[type] = "Suite"
                    it[pricePerNight] = 400.0
                }

                Rooms.insert {
                    it[hotelId] = hiltonInsertId
                    it[roomNumber] = "103"
                    it[type] = "Presidential Suite"
                    it[pricePerNight] = 600.0
                }
            }
        }
    }

    private fun getDummyHotels(): List<Hotel> {
        return listOf(
            Hotel(
                1,
                "The Grand Plaza",
                "New York, NY",
                """
                Experience unparalleled luxury in the heart of Manhattan. The Grand Plaza offers a sophisticated retreat with world-class services, including 24/7 personalized concierge assistance, valet parking, and seamless high-speed Wi-Fi throughout the property.
                
                **Location:** Located on 5th Avenue, steps away from Central Park and the Museum of Modern Art.
                
                **About Our Rooms:**
                Each room is a masterpiece of elegant design, featuring plush king-size beds with premium linens, spacious marble bathrooms with rain showers, and floor-to-ceiling windows offering breathtaking views of the New York City skyline.
                
                **Dining & Breakfast:**
                Start your day with our complimentary gourmet breakfast buffet at the rooftop 'Skyline Terrace'. Enjoy international cuisines, fresh pastries, and artisanal coffee.
                
                **Facilities & Wellness:**
                Our state-of-the-art Gym is equipped with the latest cardio machines. Relax in our temperature-controlled indoor swimming pool or rejuvenate at the full-service luxury spa.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                2,
                "Ocean View Resort",
                "Miami, FL",
                """
                Escape to a tropical paradise where stunning ocean vistas meet modern comfort. We provide exclusive private beach access, poolside drink service, and complimentary water sports equipment for all guests.
                
                **Location:** Prime beachfront property in South Beach, surrounded by vibrant nightlife and Art Deco architecture.
                
                **About Our Rooms:**
                Our oceanfront suites feature private balconies, coastal-inspired decor, and luxurious soaking tubs. Wake up to the sound of waves and enjoy the sea breeze from your terrace.
                
                **Dining & Breakfast:**
                Indulge in a lavish beachfront breakfast featuring fresh tropical fruits, custom omelets, and locally sourced seafood at our 'Blue Wave' restaurant.
                
                **Facilities & Sports:**
                Dive into our expansive infinity-edge swimming pool overlooking the Atlantic. Our wellness center includes a modern gym and yoga studio with daily classes.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                3,
                "Mountain Lodge",
                "Aspen, CO",
                """
                Discover the perfect blend of rustic charm and modern luxury. Nestled near the world's best ski slopes, we offer ski-in/ski-out access and a cozy fireplace lounge.
                
                **Location:** Situated at the base of Ajax Mountain, providing direct access to premium ski trails and hiking paths.
                
                **About Our Rooms:**
                Our lodge-style rooms feature handcrafted wooden furniture, warm stone fireplaces, and heated floors. Enjoy the comfort of premium down comforters and mountain-view terraces.
                
                **Dining & Breakfast:**
                Wake up to a hearty alpine breakfast including hot pancakes, smoked bacon, and fresh mountain berries. Evening après-ski drinks are served by the fire.
                
                **Facilities & Activities:**
                Relax in our outdoor heated swimming pool or hot tubs. Our fitness center features high-altitude training equipment. We also offer equipment rentals for skiing and mountain biking.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                4,
                "Sunset Palace",
                "Los Angeles, CA",
                """
                Experience the glamour of Hollywood at Sunset Palace. We offer a celebrity-style stay with 24-hour room service, a private screening room, and chauffeured limousine services.
                
                **Location:** Perched in the Hollywood Hills, offering iconic views of the Hollywood Sign and the sprawling LA skyline.
                
                **About Our Rooms:**
                Modern, minimalist suites with smart home automation, walk-in closets, and spa-like bathrooms. Most rooms feature expansive glass walls for the best sunset views.
                
                **Dining & Breakfast:**
                A California-fresh breakfast menu served in our sun-drenched courtyard, featuring avocado toasts, organic juices, and farm-to-table specialties.
                
                **Facilities & Wellness:**
                Our rooftop pool is a local hotspot, complete with luxury cabanas. The gym features celebrity trainers and high-intensity interval training equipment.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1582719508461-105c673771fd?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                5,
                "Riverside Inn",
                "Chicago, IL",
                """
                Enjoy a peaceful stay by the water in downtown Chicago. We offer a blend of industrial chic design and warm hospitality, with personalized city tours and boat rental services.
                
                **Location:** Located directly on the Chicago Riverwalk, within walking distance of the Magnificent Mile and Millennium Park.
                
                **About Our Rooms:**
                Industrial-style loft rooms with exposed brick, high ceilings, and floor-to-ceiling windows. Each room includes a work-friendly desk and ultra-fast Wi-Fi.
                
                **Dining & Breakfast:**
                Classic American breakfast with a Midwest twist, served in our Riverside Cafe. Don't miss our evening jazz lounge with craft cocktails.
                
                **Facilities & Sports:**
                A full-length indoor lap pool and a gym with river views. We provide complimentary bicycles for exploring the scenic Lakefront Trail.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1561501900-3701fa6a0864?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                6,
                "The Ritz Paris",
                "Paris, France",
                """
                A symbol of French 'Art de Vivre', The Ritz Paris invites you to experience timeless elegance. Our services include a personal butler for every suite and private shopping experiences.
                
                **Location:** Situated on the prestigious Place Vendôme, in the heart of the city's luxury district.
                
                **About Our Rooms:**
                Palatial rooms adorned with period furniture, crystal chandeliers, and silk wall coverings. The bathrooms feature the iconic golden swan faucets and heated marble floors.
                
                **Dining & Breakfast:**
                A refined French breakfast served in the 'Salon Proust'. Indulge in pastries from our world-renowned head pastry chef and rare tea selections.
                
                **Facilities & Wellness:**
                The 'Ritz Club & Spa' offers a stunning Roman-style swimming pool and exclusive beauty treatments. Our fitness area is equipped with state-of-the-art tech.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                7,
                "Marina Bay Sands",
                "Singapore",
                """
                The ultimate urban resort experience. Home to the world's largest rooftop infinity pool and a world-class casino, we offer unparalleled entertainment and shopping options.
                
                **Location:** An iconic landmark in Marina Bay, connected directly to a luxury mall and the Gardens by the Bay.
                
                **About Our Rooms:**
                Sleek, modern rooms with breathtaking views of the Singapore Strait or the Garden City. Features include automated curtains and premium entertainment systems.
                
                **Dining & Breakfast:**
                An extensive international breakfast buffet at 'Spago' or 'Rise', featuring Asian delicacies, European classics, and fresh tropical fruit.
                
                **Facilities & Sports:**
                The famous SkyPark infinity pool, a massive fitness center, and the Banyan Tree Spa. Guests have access to jogging tracks and yoga sessions on the observation deck.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1549294413-26f195200c16?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                8,
                "Desert Rose",
                "Dubai, UAE",
                """
                Where modern luxury meets traditional Arabian hospitality. We offer unique desert experiences, including camel treks, falconry displays, and private starlit dinners.
                
                **Location:** Set in the heart of the Dubai desert, yet only a short drive from the Burj Khalifa and the Dubai Mall.
                
                **About Our Rooms:**
                Suites designed as modern villas with Arabian motifs, private plunge pools, and outdoor terraces overlooking the sand dunes.
                
                **Dining & Breakfast:**
                Middle Eastern breakfast feast with freshly baked pita, shakshuka, and local dates. Our restaurant 'Al-Sarab' offers authentic Emirati cuisine.
                
                **Facilities & Wellness:**
                A Moroccan-inspired Hammam and spa, an outdoor swimming pool surrounded by palms, and a high-tech gym. We also organize desert safari sports.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                9,
                "Tokyo Towers",
                "Tokyo, Japan",
                """
                Experience the future of hospitality in the heart of Tokyo. Our hotel features robotic service assistants, advanced sleep technology, and a serene Zen atmosphere.
                
                **Location:** Located in the bustling Shinjuku district, near the Shinjuku Gyoen National Garden and major transit hubs.
                
                **About Our Rooms:**
                Contemporary rooms with minimalist Japanese design, featuring 'washlet' toilets, air-purifying systems, and views of the Tokyo Skytree.
                
                **Dining & Breakfast:**
                A balanced Japanese breakfast with grilled fish, miso soup, and organic rice, alongside a selection of Western favorites.
                
                **Facilities & Wellness:**
                An indoor Zen garden for meditation, a 24-hour gym, and a traditional Sento-style bath area. We also offer cultural workshops like tea ceremonies.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                10,
                "London Bridge Hotel",
                "London, UK",
                """
                Classic British charm combined with modern sophistication. Enjoy our legendary afternoon tea and personalized concierge service for West End theater bookings.
                
                **Location:** Perfectly situated south of the Thames, just a short walk from London Bridge, The Shard, and Borough Market.
                
                **About Our Rooms:**
                Elegantly appointed rooms with plush carpets, heritage-inspired decor, and high-quality British bath products.
                
                **Dining & Breakfast:**
                Full English breakfast with locally sourced sausages and eggs. Our 'Library Bar' offers an extensive collection of gins and whiskies.
                
                **Facilities & Sports:**
                A boutique fitness suite and access to local rowing clubs on the Thames. We offer guided walking tours of London's historic landmarks.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                11,
                "Sydney Opera View",
                "Sydney, Australia",
                """
                Wake up to the most iconic view in Australia. We offer exclusive harbor cruises, rooftop cocktail sessions, and private photography tours of the harbor.
                
                **Location:** Located in The Rocks district, offering unparalleled views of the Sydney Opera House and Harbor Bridge.
                
                **About Our Rooms:**
                Bright, airy rooms with nautical accents and large windows. Each room features a high-end sound system and a selection of Australian wines.
                
                **Dining & Breakfast:**
                A healthy Australian breakfast featuring smashed avocado, Vegemite on sourdough, and world-class flat whites.
                
                **Facilities & Wellness:**
                A rooftop swimming pool with harbor views and a modern gym. We also provide snorkeling gear for local beach excursions.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                12,
                "Rome Heritage Hotel",
                "Rome, Italy",
                """
                Live like royalty in the Eternal City. Our historic building combines ancient architecture with 21st-century luxury, offering private tours of the Vatican.
                
                **Location:** Just a few hundred meters from the Colosseum and the Roman Forum, in the heart of historic Rome.
                
                **About Our Rooms:**
                Rooms featuring original frescoes, antique furniture, and modern comforts like Nespresso machines and high-speed internet.
                
                **Dining & Breakfast:**
                An Italian breakfast feast with fresh cornetti, cappuccino, and seasonal fruits served on our rooftop terrace.
                
                **Facilities & Wellness:**
                A wellness center built into ancient stone vaults, including a sauna and gym. We also offer rooftop cooking classes for pasta and pizza.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1551524559-8af4e6624178?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                13,
                "Berlin Urban Stay",
                "Berlin, Germany",
                """
                A trendy, vibrant hotel for the modern traveler. We feature a revolving art gallery, an on-site vinyl record shop, and access to Berlin's exclusive clubs.
                
                **Location:** Located in the heart of Mitte, close to the Brandenburg Gate and the Museum Island.
                
                **About Our Rooms:**
                Loft-style rooms with street art murals, sustainable furniture, and premium Bluetooth speakers for your own playlists.
                
                **Dining & Breakfast:**
                A 'Berlin-style' breakfast with local cheeses, organic bread, and specialty coffee. Our bar is famous for its craft beer selection.
                
                **Facilities & Sports:**
                A rooftop basketball court and a gym with a view of the TV Tower. We offer custom bike rentals for exploring Berlin's hidden spots.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                14,
                "Cape Town Cliffside",
                "Cape Town, SA",
                """
                Breathtaking views where the mountains meet the sea. We offer guided hikes up Table Mountain and wine tasting tours to the nearby Constantia valley.
                
                **Location:** Perched on the cliffs of Bantry Bay, providing the best sunset views over the Atlantic Ocean.
                
                **About Our Rooms:**
                Luxurious suites with floor-to-ceiling glass, private balconies, and African-inspired decor. Each room has a telescope for whale watching.
                
                **Dining & Breakfast:**
                A fusion breakfast of local flavors and international favorites, featuring South African 'rooibos' tea and fresh ocean-caught fish.
                
                **Facilities & Wellness:**
                An outdoor saltwater swimming pool and a spa specializing in indigenous botanical treatments. A fully equipped gym is also available.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                15,
                "Barcelona Beach Club",
                "Barcelona, Spain",
                """
                The perfect mix of beach relaxation and city culture. We host weekly rooftop DJ sets, tapas workshops, and guided Gaudí architecture tours.
                
                **Location:** Located in the Olympic Village, just steps from the beach and a short metro ride to Las Ramblas.
                
                **About Our Rooms:**
                Stylish rooms with Mediterranean colors, rain showers, and smart TVs. Many rooms offer views of both the sea and the Sagrada Familia.
                
                **Dining & Breakfast:**
                A Mediterranean breakfast buffet with Spanish ham, fresh pan con tomate, and seasonal fruits.
                
                **Facilities & Sports:**
                A beach-view gym and two swimming pools. We offer paddleboarding lessons and have a private area on the beach for guests.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1564501049412-61c2a3083791?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                16,
                "Venice Canal Suite",
                "Venice, Italy",
                """
                Experience the magic of Venice from your own private suite. We offer private gondola arrivals, Murano glass-making tours, and secret garden dinners.
                
                **Location:** Overlooking the Grand Canal, just a short walk from St. Mark's Square and the Rialto Bridge.
                
                **About Our Rooms:**
                Romantic suites with silk drapery, Murano glass chandeliers, and original wooden beams. Every room has a unique view of the canals.
                
                **Dining & Breakfast:**
                A Venetian breakfast served in your room or in our hidden courtyard garden, featuring artisanal pastries and Italian espresso.
                
                **Facilities & Wellness:**
                A boutique spa offering aromatherapy and massage. While we don't have a gym on-site, we offer guided 'hidden Venice' walking tours.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                17,
                "Bangkok Skyline",
                "Bangkok, Thailand",
                """
                A high-rise sanctuary in the heart of the city. We offer traditional Thai massage services, tuk-tuk shuttles to local markets, and a sky-high cocktail bar.
                
                **Location:** Located in the Sukhumvit business district, with direct access to the Skytrain (BTS) for easy city travel.
                
                **About Our Rooms:**
                Modern, tech-savvy rooms with automated features, premium bedding, and panoramic views of Bangkok's vibrant skyline.
                
                **Dining & Breakfast:**
                An incredible spread of Thai street food favorites and international dishes for breakfast. Don't miss our 'Mango Sticky Rice' station.
                
                **Facilities & Sports:**
                A rooftop infinity pool, a Muay Thai boxing gym, and a full-service spa. Guests can enjoy yoga classes at sunrise on the terrace.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1563911302283-d2bc129e7570?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                18,
                "Istanbul Terrace",
                "Istanbul, Turkey",
                """
                Where East meets West in luxurious style. We offer authentic Turkish bath experiences, carpet-weaving demonstrations, and Bosphorus boat tours.
                
                **Location:** Located in Sultanahmet, overlooking the Blue Mosque and the Hagia Sophia.
                
                **About Our Rooms:**
                Rooms decorated with Turkish textiles and hand-painted ceramics, featuring modern amenities and balconies with historic views.
                
                **Dining & Breakfast:**
                A traditional Turkish 'Kahvalti' breakfast with various cheeses, olives, honey, and freshly brewed Turkish tea.
                
                **Facilities & Wellness:**
                An on-site Hamam (Turkish bath), a fitness center, and a rooftop terrace for evening relaxation. We offer guided history walks.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1618773928121-c32242e63f39?q=80&w=400&auto=format&fit=crop"
            ),
            Hotel(
                19,
                "Dublin Manor",
                "Dublin, Ireland",
                """
                Quiet luxury in a historic Georgian building. Enjoy our private library, whiskey tasting sessions, and afternoon tea by the roaring fireplace.
                
                **Location:** Situated on a quiet square in the heart of Dublin, near Trinity College and St. Stephen's Green.
                
                **About Our Rooms:**
                Classic rooms with antique furniture, heavy drapes, and premium Irish linens. Each room includes a selection of Irish literature.
                
                **Dining & Breakfast:**
                A hearty Irish breakfast with soda bread, black pudding, and locally sourced butter. Our bar specializes in rare Irish whiskies.
                
                **Facilities & Wellness:**
                A small fitness suite and access to local golf clubs. We provide umbrellas and wellies for exploring the beautiful Irish countryside.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1742171046853-0961eabdc7d4?w=500&auto=format&fit=crop"
            ),
            Hotel(
                20,
                "Prague Old Town",
                "Prague, Czechia",
                """
                Experience the charm and history of the Old Town. We feature an on-site microbrewery, marionette show bookings, and private tours of Prague Castle.
                
                **Location:** Located in the heart of the Old Town, just steps from the Astronomical Clock and the Charles Bridge.
                
                **About Our Rooms:**
                Charming rooms with Gothic architectural details, wooden ceilings, and modern bathrooms with organic toiletries.
                
                **Dining & Breakfast:**
                A traditional Czech breakfast with various breads, meats, and pastries. Enjoy a fresh beer from our brewery in the evening.
                
                **Facilities & Wellness:**
                A wellness center with a beer spa and a modern gym. We also offer walking tours focused on Prague's rich legends and history.
            """.trimIndent(),
                "https://images.unsplash.com/photo-1611892440504-42a792e24d32?q=80&w=400&auto=format&fit=crop"
            )
        )
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        // In a real app, use environment variables for these
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/hotel_db"
        config.username = "postgres"
        config.password = "password"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val firstName = varchar("firstname", 128)
    val lastName = varchar("lastname", 128)
    val email = varchar("email", 128).uniqueIndex()
    val password = varchar("password", 128)
    val phone = varchar("phone", 10).uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}

object Hotels : Table("hotels") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val location = varchar("location", 255)
    val description = text("description")
    val imageUrl = varchar("image_url", 512).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Rooms : Table("rooms") {
    val id = integer("id").autoIncrement()
    val hotelId = integer("hotel_id").references(Hotels.id)
    val roomNumber = varchar("room_number", 50)
    val type = varchar("type", 50)
    val pricePerNight = double("price_per_night")
    val isAvailable = bool("is_available").default(true)

    override val primaryKey = PrimaryKey(id)
}

object Bookings : Table("bookings") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val roomId = integer("room_id").references(Rooms.id)
    val checkInDate = varchar("check_in_date", 50) // Simplified as String for now
    val checkOutDate = varchar("check_out_date", 50)
    val totalPrice = double("total_price")
    val status = varchar("status", 50).default("PENDING")

    override val primaryKey = PrimaryKey(id)
}
