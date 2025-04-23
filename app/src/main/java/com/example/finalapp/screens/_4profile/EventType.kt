package com.example.finalapp.screens._4profile

enum class EventTopic(val category: String) {
    // Social & Entertainment
    BIRTHDAY_PARTY("Social & Entertainment"),
    HOUSEWARMING("Social & Entertainment"),
    GRADUATION_PARTY("Social & Entertainment"),
    GAME_NIGHT("Social & Entertainment"),
    MOVIE_NIGHT("Social & Entertainment"),
    CONCERT("Social & Entertainment"),
    FESTIVAL("Social & Entertainment"),
    NIGHT_OUT("Social & Entertainment"),
    KARAOKE("Social & Entertainment"),

    // Education & Learning
    WORKSHOP("Education & Learning"),
    WEBINAR("Education & Learning"),
    STUDY_GROUP("Education & Learning"),
    BOOK_CLUB("Education & Learning"),
    LANGUAGE_EXCHANGE("Education & Learning"),
    LECTURE("Education & Learning"),

    // Professional & Networking
    BUSINESS_CONFERENCE("Professional & Networking"),
    NETWORKING_EVENT("Professional & Networking"),
    CAREER_FAIR("Professional & Networking"),
    PRODUCT_LAUNCH("Professional & Networking"),
    INDUSTRY_PANEL("Professional & Networking"),
    STARTUP_PITCH("Professional & Networking"),
    COWORKING_MEETUP("Professional & Networking"),

    // Health & Wellness
    YOGA_CLASS("Health & Wellness"),
    MEDITATION_SESSION("Health & Wellness"),
    FITNESS_BOOTCAMP("Health & Wellness"),
    WELLNESS_RETREAT("Health & Wellness"),
    MENTAL_HEALTH_TALK("Health & Wellness"),
    SUPPORT_CIRCLE("Health & Wellness"),

    // Hobbies & DIY
    ART_WORKSHOP("Hobbies & DIY"),
    COOKING_CLASS("Hobbies & DIY"),
    PHOTOGRAPHY_MEETUP("Hobbies & DIY"),
    GARDENING_EVENT("Hobbies & DIY"),
    MAKER_FAIR("Hobbies & DIY"),
    KNITTING_CIRCLE("Hobbies & DIY"),

    // Travel & Outdoors
    HIKING_TRIP("Travel & Outdoors"),
    CAMPING_EVENT("Travel & Outdoors"),
    ROAD_TRIP("Travel & Outdoors"),
    BEACH_DAY("Travel & Outdoors"),
    CITY_TOUR("Travel & Outdoors"),
    NATURE_WALK("Travel & Outdoors"),
    TRAVEL_PLANNING_MEETUP("Travel & Outdoors"),

    // Community & Causes
    VOLUNTEERING("Community & Causes"),
    CHARITY_EVENT("Community & Causes"),
    FUNDRAISER("Community & Causes"),
    COMMUNITY_CLEANUP("Community & Causes"),
    PROTEST("Community & Causes"),
    TOWN_HALL("Community & Causes"),
    RELIGIOUS_GATHERING("Community & Causes"),

    // Gaming & Esports
    GAMING_TOURNAMENT("Gaming & Esports"),
    LAN_PARTY("Gaming & Esports"),
    WATCH_PARTY("Gaming & Esports"),
    TABLETOP_GAME_NIGHT("Gaming & Esports"),
    GAME_DEV_MEETUP("Gaming & Esports"),

    // Art & Culture
    ART_EXHIBITION("Art & Culture"),
    THEATER_PERFORMANCE("Art & Culture"),
    OPEN_MIC("Art & Culture"),
    POETRY_SLAM("Art & Culture"),
    CULTURAL_FESTIVAL("Art & Culture"),
    DANCE_PERFORMANCE("Art & Culture"),
    MUSEUM_MEETUP("Art & Culture"),

    // Dating & Relationships
    SINGLES_MIXER("Dating & Relationships"),
    SPEED_DATING("Dating & Relationships"),
    COUPLES_RETREAT("Dating & Relationships"),
    RELATIONSHIP_WORKSHOP("Dating & Relationships"),

    // Special Interest / Niche
    ANIME_MEETUP("Special Interest"),
    COMIC_CON("Special Interest"),
    PET_MEETUP("Special Interest"),
    CAR_SHOW("Special Interest"),
    TECH_EVENT("Special Interest"),
    HACKATHON("Special Interest"),
    CRYPTO_MEETUP("Special Interest");
}
