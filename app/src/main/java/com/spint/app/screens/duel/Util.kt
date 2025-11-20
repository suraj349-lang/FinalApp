package com.spint.app.screens.duel

sealed class MatchCategory(
    val title: String,
    val subtopics: List<MatchSubtopic> = emptyList()
) {
    data class MoodCategory(val moods: List<MoodItem>) :
        MatchCategory(title = "Mood")

    data object Sports : MatchCategory(
        "Sports",
        listOf(
            MatchSubtopic("Football"),
            MatchSubtopic("Cricket"),
            MatchSubtopic("Basketball"),
            MatchSubtopic("Tennis"),
            MatchSubtopic("F1 Racing"),
            MatchSubtopic("Esports")
        )
    )

    data object Entertainment : MatchCategory(
        "Entertainment",
        listOf(
            MatchSubtopic("Movies"),
            MatchSubtopic("Anime"),
            MatchSubtopic("Music"),
            MatchSubtopic("Gaming"),
            MatchSubtopic("Celebrities"),
            MatchSubtopic("K-Pop")
        )
    )

    data object Lifestyle : MatchCategory(
        "Lifestyle",
        listOf(
            MatchSubtopic("Travel"),
            MatchSubtopic("Fashion"),
            MatchSubtopic("Food & Cooking"),
            MatchSubtopic("Health"),
            MatchSubtopic("Nightlife")
        )
    )

    data object Technology : MatchCategory(
        "Technology",
        listOf(
            MatchSubtopic("AI & ML"),
            MatchSubtopic("Gadgets"),
            MatchSubtopic("Programming"),
            MatchSubtopic("Startups"),
            MatchSubtopic("Crypto")
        )
    )

    data object Relationships : MatchCategory(
        "Relationships",
        listOf(
            MatchSubtopic("Crush"),
            MatchSubtopic("Breakup"),
            MatchSubtopic("Dating"),
            MatchSubtopic("Advice"),
            MatchSubtopic("Confusion"),
        )
    )

    data object RandomFun : MatchCategory(
        "Random Fun",
        listOf(
            MatchSubtopic("Truth or Dare"),
            MatchSubtopic("Would You Rather"),
            MatchSubtopic("Confessions"),
            MatchSubtopic("Dark Humor"),
            MatchSubtopic("Roast Mode")
        )
    )
}

data class MatchSubtopic(val name: String)

data class MoodItem(
    val colorHex: String,
    val label: String
)
