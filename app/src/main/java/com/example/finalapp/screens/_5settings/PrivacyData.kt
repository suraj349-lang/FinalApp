package com.example.finalapp.screens._5settings


data class PrivacyPolicySection(val title: String, val content: String)

val privacyPolicySections = listOf(
    PrivacyPolicySection("Effective Date", "**Effective Date:** [Date]\n**Last Updated:** [Date]"),

    PrivacyPolicySection("Introduction",
        "Welcome to **[Your App Name]** ('we,' 'our,' or 'us'). Your privacy is important to us. This Privacy Policy explains how we collect, use, and protect your information when you use our social media platform."),

    PrivacyPolicySection("1. Information We Collect",
        "**1.1 Information You Provide**\n" +
                "- **Account Information:** When you sign up, we collect your **name, email, phone number, username, and profile picture**.\n" +
                "- **Content:** Posts, comments, likes, and media (images, videos, etc.) you share.\n" +
                "- **Messages:** If you use direct messaging, we collect and store your messages securely.\n\n" +
                "**1.2 Information We Collect Automatically**\n" +
                "- **Device Information:** IP address, device type, OS version, and app usage statistics.\n" +
                "- **Location Data:** If you allow location services, we may collect and use your **approximate or precise location**.\n" +
                "- **Cookies & Tracking:** We use cookies and similar technologies to improve your experience.\n\n" +
                "**1.3 Third-Party Data**\n" +
                "- If you log in via **Google, Facebook, or Apple**, we receive some of your profile details from these services."
    ),

    PrivacyPolicySection("2. How We Use Your Information",
        "We use your data to:\n" +
                "✅ Provide and improve our services.\n" +
                "✅ Personalize your feed and recommendations.\n" +
                "✅ Facilitate connections between users.\n" +
                "✅ Monitor and prevent fraudulent activity.\n" +
                "✅ Send notifications about updates, offers, or changes."
    ),

    PrivacyPolicySection("3. How We Share Your Information",
        "We **do not sell** your data. However, we may share it in the following cases:\n" +
                "🔹 **With other users:** Public posts and profile data are visible to others.\n" +
                "🔹 **With third-party service providers:** For analytics, cloud storage, and security.\n" +
                "🔹 **With legal authorities:** If required by law or to prevent harm."
    ),

    PrivacyPolicySection("4. Data Retention & Security",
        "- We **store your data** as long as your account is active.\n" +
                "- We use **encryption, access controls, and monitoring** to secure your data.\n" +
                "- You can **request data deletion** via settings or by contacting us."
    ),

    PrivacyPolicySection("5. Your Rights & Choices",
        "You have the right to:\n" +
                "✔ **Access & update** your data.\n" +
                "✔ **Delete your account & data** upon request.\n" +
                "✔ **Control notifications & permissions** (e.g., location access)."
    ),

    PrivacyPolicySection("6. Third-Party Links & Services",
        "Your interactions with external links, ads, or third-party integrations (e.g., YouTube, Instagram) are subject to their own **privacy policies**."
    ),

    PrivacyPolicySection("7. Changes to This Policy",
        "We may update this policy. We will notify you of significant changes via email or in-app notifications."
    ),

    PrivacyPolicySection("8. Contact Us",
        "If you have questions about this Privacy Policy, contact us at:\n" +
                "📩 **Email:** [your_email@example.com]\n" +
                "🌐 **Website:** [yourapp.com]"
    )
)
