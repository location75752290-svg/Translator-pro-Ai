package com.example.data.model

data class RoleplayCharacter(
    val id: String,
    val title: String,
    val urduTitle: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val avatarEmoji: String,
    val initialEnglish: String,
    val initialUrdu: String,
    val systemPrompt: String
) {
    companion object {
        val characters = listOf(
            RoleplayCharacter(
                id = "teacher",
                title = "Teacher",
                urduTitle = "استانی / استاد",
                name = "Prof. Sarah",
                subtitle = "English Teacher",
                description = "Ask any English grammar or vocabulary question and practice with Prof. Sarah!",
                avatarEmoji = "👩‍🏫",
                initialEnglish = "Hello! I am Prof. Sarah, your English Teacher. Ask me anything, or practice with me!\nExample: \"Practicing English every day makes you perfect.\"\nDo you want to practice this sentence?",
                initialUrdu = "ہیلو! میں پروفیسر سارہ ہوں، آپ کی انگلش ٹیچر۔ مجھ سے کچھ بھی پوچھیں، یا میرے ساتھ مشق کریں!\nمثال: \"روزانہ انگریزی کی مشق کرنا آپ کو کامل بناتا ہے۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Prof. Sarah, an English Teacher. You are a friendly and helpful AI."
            ),
            RoleplayCharacter(
                id = "doctor",
                title = "Doctor",
                urduTitle = "ڈاکٹر",
                name = "Dr. Ali",
                subtitle = "Doctor",
                description = "Ask about health, symptoms, and medical advice explained in simple words.",
                avatarEmoji = "👨‍⚕️",
                initialEnglish = "Hello! I am Dr. Ali, your Doctor. I can explain health topics to you in simple words.\nExample: \"Drinking plenty of water is good for your health.\"\nDo you want to practice this sentence?",
                initialUrdu = "ہیلو! میں ڈاکٹر علی ہوں، آپ کا ڈاکٹر۔ میں آپ کو صحت کے موضوعات آسان الفاظ میں سمجھا سکتا ہوں۔\nمثال: \"کثرت سے پانی پینا آپ کی صحت کے لیے اچھا ہے۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Dr. Ali, a Doctor who explains health in simple words. You are a friendly and helpful AI."
            ),
            RoleplayCharacter(
                id = "shopkeeper",
                title = "Shopkeeper",
                urduTitle = "دوکاندار",
                name = "Ahmed Bhai",
                subtitle = "Shopkeeper",
                description = "Talk about shopping, items, prices, and bargains at the local store.",
                avatarEmoji = "🏪",
                initialEnglish = "Hello! I am Ahmed Bhai, your Shopkeeper. Welcome to my shop, let's talk about shopping.\nExample: \"We have a great discount on fresh fruits today.\"\nDo you want to practice this sentence?",
                initialUrdu = "ہیلو! میں احمد بھائی ہوں، آپ کا دکاندار۔ میری دکان میں خوش آمدید، آئیے شاپنگ کے بارے میں بات کرتے ہیں۔\nمثال: \"آج ہمارے پاس تازہ پھلوں پر بہترین ڈسکاؤنٹ ہے۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Ahmed Bhai, a Shopkeeper who talks about shopping. You are a friendly and helpful AI."
            ),
            RoleplayCharacter(
                id = "friend",
                title = "Friend",
                urduTitle = "دوست",
                name = "Mike",
                subtitle = "Best Friend",
                description = "Have a casual friendly chat about hobbies, life, and weekend plans.",
                avatarEmoji = "👬",
                initialEnglish = "Hey there! I am Mike, your Best Friend. Let's have a casual chat about anything.\nExample: \"Let's hang out at our favorite cafe this weekend.\"\nDo you want to practice this sentence?",
                initialUrdu = "ہیلو دوست! میں مائیک ہوں، آپ کا بہترین دوست۔ آئیے کسی بھی چیز کے بارے میں عام بات چیت کریں۔\nمثال: \"آئیے اس ویک اینڈ پر اپنے پسندیدہ کیفے میں ملتے ہیں۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Mike, a Best Friend who loves casual chat. You are a friendly and helpful AI."
            ),
            RoleplayCharacter(
                id = "boss",
                title = "Boss",
                urduTitle = "باس / مینیجر",
                name = "Mr. Khan",
                subtitle = "Office Boss",
                description = "Talk about formal workplace tasks, project updates, and business meetings.",
                avatarEmoji = "👨‍💼",
                initialEnglish = "Good morning. I am Mr. Khan, your Office Boss. Let's discuss our workplace tasks and updates.\nExample: \"Please complete the project report by tomorrow morning.\"\nDo you want to practice this sentence?",
                initialUrdu = "صبح بخیر۔ میں مسٹر خان ہوں، آپ کا آفس باس۔ آئیے ہمارے کام کے کاموں اور اپ ڈیٹس پر بات کرتے ہیں۔\nمثال: \"براہ کرم کل صبح تک پروجیکٹ کی رپورٹ مکمل کریں۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Mr. Khan, an Office Boss who talks about work. You are a friendly and helpful AI."
            ),
            RoleplayCharacter(
                id = "tourist_guide",
                title = "Tourist Guide",
                urduTitle = "ٹورسٹ گائیڈ",
                name = "Lisa",
                subtitle = "Tourist Guide",
                description = "Ask about famous landmarks, historical sights, directions, and travel tips.",
                avatarEmoji = "🧭",
                initialEnglish = "Hello! I am Lisa, your Tourist Guide. I love talking about beautiful travel destinations.\nExample: \"The historic fort is the most popular tourist spot in the city.\"\nDo you want to practice this sentence?",
                initialUrdu = "ہیلو! میں لیزا ہوں، آپ کی ٹورسٹ گائیڈ۔ مجھے خوبصورت سفری مقامات کے بارے میں بات کرنا پسند ہے۔\nمثال: \"تاریخی قلعہ شہر کا سب سے مشہور سیاحتی مقام ہے۔\"\nکیا آپ اسکو پریکٹس کرنا چاہیں گے؟",
                systemPrompt = "You are Lisa, a Tourist Guide who talks about travel. You are a friendly and helpful AI."
            )
        )
    }
}

data class RoleplayMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val characterId: String,
    val sender: String, // "user" or "character"
    val englishText: String,
    val urduText: String,
    val timestamp: Long = System.currentTimeMillis()
)
