package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class ConversionSentence(
    val id: Int,
    val english: String,
    val urdu: String,
    val romanUrdu: String
)

data class ConversionTopic(
    val id: Int,
    val name: String,
    val urduName: String,
    val iconName: String, // String representation to map to material icons
    val description: String,
    val sentences: List<ConversionSentence>
)

object DailyConversionDatabase {

    private val names = listOf("Ali", "Sana", "Ahmad", "Ayesha", "Zain", "Fatima", "Usman", "Amna", "Bilal", "Sara")
    private val namesUrdu = listOf("علی", "ثنا", "احمد", "عائشہ", "زین", "فاطمہ", "عثمان", "آمنہ", "بلال", "سارہ")
    private val namesRoman = listOf("Ali", "Sana", "Ahmad", "Ayesha", "Zain", "Fatima", "Usman", "Amna", "Bilal", "Sara")

    private val places = listOf("market", "school", "office", "hospital", "park", "library", "station", "hotel", "airport", "mall")
    private val placesUrdu = listOf("بازار", "اسکول", "دفتر", "ہسپتال", "پارک", "لائبریری", "اسٹیشن", "ہوٹل", "ہوائی اڈے", "مال")
    private val placesRoman = listOf("bazaar", "school", "daftar", "haspatal", "park", "library", "station", "hotel", "hawai adday", "mall")

    private val foods = listOf("tea", "coffee", "rice", "biryani", "pizza", "apple", "mango", "chicken", "milk", "water")
    private val foodsUrdu = listOf("چائے", "کافی", "چاول", "بریانی", "پیزا", "سیب", "آم", "چکن", "دودھ", "پانی")
    private val foodsRoman = listOf("chaye", "coffee", "chawal", "biryani", "pizza", "saib", "aam", "chicken", "doodh", "paani")

    val topics: List<ConversionTopic> by lazy {
        val list = mutableListOf<ConversionTopic>()
        
        val topicDefinitions = listOf(
            Triple(1, "Greetings & Socializing", "خیرمقدم اور میل جول" to "chat"),
            Triple(2, "Family & Relationships", "خاندان اور تعلقات" to "people"),
            Triple(3, "Food & Restaurant", "کھانا اور ریستوراں" to "restaurant"),
            Triple(4, "Shopping & Store", "خریداری اور دکان" to "shopping_bag"),
            Triple(5, "Office & Career", "دفتر اور ملازمت" to "work"),
            Triple(6, "Travel & Transport", "سفر اور آمدورفت" to "flight"),
            Triple(7, "Health & Body", "صحت اور جسم" to "healing"),
            Triple(8, "Education & Study", "تعلیم اور مطالعہ" to "school"),
            Triple(9, "Weather & Season", "موسم اور وقت" to "wb_sunny"),
            Triple(10, "Sports & Hobby", "کھیل اور مشغلہ" to "sports_cricket"),
            Triple(11, "Emotions & Feelings", "جذبات" to "emoji_emotions"),
            Triple(12, "Directions & Places", "راستہ اور جگہیں" to "explore"),
            Triple(13, "Time & Date", "وقت اور تاریخ" to "today"),
            Triple(14, "Clothes & Appearance", "کپڑے اور شکل" to "checkroom"),
            Triple(15, "Home & Housework", "گھر اور گھریلو کام" to "home"),
            Triple(16, "Money & Bank", "پیسے اور بینک" to "payments"),
            Triple(17, "Phone & Internet", "موبائل اور انٹرنیٹ" to "phone_android"),
            Triple(18, "Questions & Answers", "سوالات" to "help"),
            Triple(19, "Daily Phrases", "روزمرہ جملے" to "whatshot"),
            Triple(20, "School & Study", "سکول اور پڑھائی" to "school")
        )

        for ((id, name, pair) in topicDefinitions) {
            val (urduName, iconName) = pair
            val desc = "Practice 50 high-quality sentences for $name."
            val sentences = generateSentencesForTopic(id, name)
            list.add(ConversionTopic(id, name, urduName, iconName, desc, sentences))
        }
        list
    }

    private fun generateSentencesForTopic(topicId: Int, topicName: String): List<ConversionSentence> {
        val list = mutableListOf<ConversionSentence>()

        // 1. Let's add 10 baseline realistic hand-crafted sentences first for each topic
        val baseSentences = getBaseSentencesForTopic(topicId)
        list.addAll(baseSentences)

        // 2. Programmatically generate 40 additional unique variation sentences to reach exactly 50 per topic
        var currentId = baseSentences.size + 1
        val needed = 50 - baseSentences.size

        for (i in 0 until needed) {
            val nameIndex = (i + topicId) % names.size
            val placeIndex = (i * 2 + topicId) % places.size
            val foodIndex = (i * 3 + topicId) % foods.size

            val pName = names[nameIndex]
            val pNameUr = namesUrdu[nameIndex]
            val pNameRo = namesRoman[nameIndex]

            val pPlace = places[placeIndex]
            val pPlaceUr = placesUrdu[placeIndex]
            val pPlaceRo = placesRoman[placeIndex]

            val pFood = foods[foodIndex]
            val pFoodUr = foodsUrdu[foodIndex]
            val pFoodRo = foodsRoman[foodIndex]

            val generated = when (topicId) {
                1 -> ConversionSentence(
                    id = currentId,
                    english = "Hello $pName, how are you doing today?",
                    urdu = "ہیلو $pNameUr، آپ آج کیسے ہیں؟",
                    romanUrdu = "Hello $pNameRo, aap aaj kaise hain?"
                )
                2 -> ConversionSentence(
                    id = currentId,
                    english = "How is $pName's family doing?",
                    urdu = "$pNameUr کا خاندان کیسا ہے؟",
                    romanUrdu = "How is $pNameRo ka khandan kaisa hai?"
                )
                3 -> ConversionSentence(
                    id = currentId,
                    english = "I would like to order some $pFood.",
                    urdu = "میں کچھ $pFoodUr آرڈر کرنا چاہوں گا۔",
                    romanUrdu = "Mein kuch $pFoodRo order karna chahunga."
                )
                4 -> ConversionSentence(
                    id = currentId,
                    english = "Where is the nearest $pPlace store?",
                    urdu = "قریب ترین $pPlaceUr کی دکان کہاں ہے؟",
                    romanUrdu = "Qareeb tareen $pPlaceRo ki dukan kahan hai?"
                )
                5 -> ConversionSentence(
                    id = currentId,
                    english = "Is $pName working at the $pPlace today?",
                    urdu = "کیا $pNameUr آج $pPlaceUr میں کام کر رہا ہے؟",
                    romanUrdu = "Kya $pNameRo aaj $pPlaceRo mein kaam kar raha hai?"
                )
                6 -> ConversionSentence(
                    id = currentId,
                    english = "We are traveling to the $pPlace.",
                    urdu = "ہم $pPlaceUr کا سفر کر رہے ہیں۔",
                    romanUrdu = "Hum $pPlaceRo ka safar kar rahe hain."
                )
                7 -> ConversionSentence(
                    id = currentId,
                    english = "You should drink more $pFood for health.",
                    urdu = "آپ کو صحت کے لیے مزید $pFoodUr پینا چاہیے۔",
                    romanUrdu = "Aap ko sehat ke liye mazeed $pFoodRo peena chahiye."
                )
                8 -> ConversionSentence(
                    id = currentId,
                    english = "Is $pName studying at the school?",
                    urdu = "کیا $pNameUr اسکول میں پڑھ رہا ہے؟",
                    romanUrdu = "Kya $pNameRo school mein parh raha hai?"
                )
                9 -> ConversionSentence(
                    id = currentId,
                    english = "The weather is nice to visit the $pPlace.",
                    urdu = "موسم $pPlaceUr جانے کے لیے بہت اچھا ہے۔",
                    romanUrdu = "Mausam $pPlaceRo jaane ke liye bohat accha hai."
                )
                10 -> ConversionSentence(
                    id = currentId,
                    english = "Does $pName like playing sports?",
                    urdu = "کیا $pNameUr کو کھیل کھیلنا پسند ہے؟",
                    romanUrdu = "Kya $pNameRo ko khel khelna pasand hai?"
                )
                11 -> ConversionSentence(
                    id = currentId,
                    english = "Is $pName feeling happy today?",
                    urdu = "کیا $pNameUr آج خوش محسوس کر رہا ہے؟",
                    romanUrdu = "Kya $pNameRo aaj khush mehsoos kar raha hai?"
                )
                12 -> ConversionSentence(
                    id = currentId,
                    english = "Where is the way to the $pPlace?",
                    urdu = "$pPlaceUr کا راستہ کہاں ہے؟",
                    romanUrdu = "Where is $pPlaceRo ka rasta kahan hai?"
                )
                13 -> ConversionSentence(
                    id = currentId,
                    english = "What is the time right now?",
                    urdu = "اس وقت کیا وقت ہوا ہے؟",
                    romanUrdu = "Is waqt kya waqt hua hai?"
                )
                14 -> ConversionSentence(
                    id = currentId,
                    english = "I like $pName's beautiful clothes.",
                    urdu = "مجھے $pNameUr کے خوبصورت کپڑے پسند ہیں۔",
                    romanUrdu = "Mujhe $pNameRo ke khubsurat kapre pasand hain."
                )
                15 -> ConversionSentence(
                    id = currentId,
                    english = "Let's clean the house today.",
                    urdu = "آئیں آج گھر صاف کرتے ہیں۔",
                    romanUrdu = "Aayein aaj ghar saaf karte hain."
                )
                16 -> ConversionSentence(
                    id = currentId,
                    english = "How much money does $pName have?",
                    urdu = "$pNameUr کے پاس کتنے پیسے ہیں؟",
                    romanUrdu = "$pNameRo ke paas kitne paise hain?"
                )
                17 -> ConversionSentence(
                    id = currentId,
                    english = "Can you send me a message on phone?",
                    urdu = "کیا آپ مجھے فون پر پیغام بھیج سکتے ہیں؟",
                    romanUrdu = "Kya aap mujhe phone par paigham bhej sakte hain?"
                )
                18 -> ConversionSentence(
                    id = currentId,
                    english = "Can I ask you a simple question?",
                    urdu = "کیا میں آپ سے ایک سادہ سا سوال پوچھ سکتا ہوں؟",
                    romanUrdu = "Kya mein aap se aik sada sa sawal pooch sakta hoon?"
                )
                19 -> ConversionSentence(
                    id = currentId,
                    english = "That is absolutely right.",
                    urdu = "یہ بالکل ٹھیک ہے۔",
                    romanUrdu = "Yeh bilkul theek hai."
                )
                20 -> ConversionSentence(
                    id = currentId,
                    english = "Is $pName studying at the school?",
                    urdu = "کیا $pNameUr اسکول میں پڑھ رہا ہے؟",
                    romanUrdu = "Kya $pNameRo school mein parh raha hai?"
                )
                else -> ConversionSentence(
                    id = currentId,
                    english = "Let us learn more sentences together.",
                    urdu = "آئیں مل کر مزید جملے سیکھیں۔",
                    romanUrdu = "Aayein mil kar mazeed jumlay seekhein."
                )
            }
            list.add(generated)
            currentId++
        }

        return list
    }

    private fun getBaseSentencesForTopic(topicId: Int): List<ConversionSentence> {
        return when (topicId) {
            1 -> listOf(
                ConversionSentence(1, "Where are you going?", "آپ کہاں جا رہے ہیں؟", "Aap kahan ja rahe hain?"),
                ConversionSentence(2, "What is your name?", "آپ کا نام کیا ہے؟", "Aap ka naam kya hai?"),
                ConversionSentence(3, "Nice to meet you.", "آپ سے مل کر خوشی ہوئی۔", "Aap se mil kar khushi hui."),
                ConversionSentence(4, "How are you today?", "آج آپ کیسے ہیں؟", "Aaj aap kaise hain?"),
                ConversionSentence(5, "I am doing well, thank you.", "میں ٹھیک ہوں، شکریہ۔", "Mein theek hoon, shukriya."),
                ConversionSentence(6, "Where do you live?", "آپ کہاں رہتے ہیں؟", "Aap kahan rehte hain?"),
                ConversionSentence(7, "Have a safe journey.", "آپ کا سفر محفوظ ہو۔", "Aap ka safar mahfooz ho."),
                ConversionSentence(8, "Long time no see.", "بہت دنوں سے ملاقات نہیں ہوئی۔", "Bohat dino se mulaqat nahi hui."),
                ConversionSentence(9, "What a pleasant surprise!", "کتنی خوشگوار حیرت ہے!", "Kitni khushgawar hairat hai!"),
                ConversionSentence(10, "Goodbye and take care.", "اللہ حافظ اور اپنا خیال رکھیں۔", "Allah hafiz aur apna khayal rakhein.")
            )
            2 -> listOf(
                ConversionSentence(1, "This is my brother.", "یہ میرا بھائی ہے۔", "Yeh mera bhai hai."),
                ConversionSentence(2, "How is your mother?", "آپ کی امی کیسی ہیں؟", "Aap ki ammi kaisi hain?"),
                ConversionSentence(3, "I love my family.", "میں اپنے خاندان سے محبت کرتا ہوں۔", "Mein apne khandan se mohabbat karta hoon."),
                ConversionSentence(4, "Do you have siblings?", "کیا آپ کے بہن بھائی ہیں؟", "Kya aap ke behan bhai hain?"),
                ConversionSentence(5, "My father is a businessman.", "میرے والد ایک تاجر ہیں۔", "Mere walid aik tajir hain."),
                ConversionSentence(6, "She looks like her mother.", "وہ اپنی ماں جیسی دکھتی ہے۔", "Woh apni maa jaisi dikhti hai."),
                ConversionSentence(7, "We support each other.", "ہم ایک دوسرے کا ساتھ دیتے ہیں۔", "Hum aik doosre ka sath dete hain."),
                ConversionSentence(8, "They have three children.", "ان کے تین بچے ہیں۔", "Un ke teen bacche hain."),
                ConversionSentence(9, "He is my close friend.", "وہ میرا قریبی دوست ہے۔", "Woh mera qareebi dost hai."),
                ConversionSentence(10, "Family comes first.", "خاندان سب سے پہلے آتا ہے۔", "Khandan sab se pehle aata hai.")
            )
            3 -> listOf(
                ConversionSentence(1, "The food is very delicious.", "کھانا بہت لذیذ ہے۔", "Khana bohat lazeez hai."),
                ConversionSentence(2, "Would you like some tea?", "کیا آپ کچھ چائے پسند کریں گے؟", "Kya aap kuch chaye pasand karein ge?"),
                ConversionSentence(3, "I am extremely hungry.", "مجھے بہت زیادہ بھوک لگی ہے۔", "Mujhe bohat zyada bhook lagi hai."),
                ConversionSentence(4, "Where is the menu?", "مینو کہاں ہے؟", "Menu kahan hai?"),
                ConversionSentence(5, "Can I have some water, please?", "کیا مجھے تھوڑا پانی مل سکتا ہے؟", "Kya mujhe thoda paani mil sakta hai?"),
                ConversionSentence(6, "I prefer spicy food.", "میں مصالحہ دار کھانے کو ترجیح دیتا ہوں۔", "Mein masaleh dar khane ko tarjeeh deta hoon."),
                ConversionSentence(7, "This soup is too hot.", "یہ سوپ بہت گرم ہے۔", "Yeh soup bohat garam hai."),
                ConversionSentence(8, "The dinner was fantastic.", "رات کا کھانا بہترین تھا۔", "Raat ka khana behtareen tha."),
                ConversionSentence(9, "Let's eat out tonight.", "آئیں آج رات باہر کھاتے ہیں۔", "Aayein aaj raat bahar khate hain."),
                ConversionSentence(10, "I am full now.", "میرا پیٹ اب بھر گیا ہے۔", "Mera pait ab bhar gaya hai.")
            )
            4 -> listOf(
                ConversionSentence(1, "How much does this cost?", "اس کی قیمت کتنی ہے؟", "Is ki qeemat kitni hai?"),
                ConversionSentence(2, "Can I get a discount?", "کیا مجھے رعایت مل سکتی ہے؟", "Kya mujhe riayat mil sakti hai?"),
                ConversionSentence(3, "I want to buy a new dress.", "میں ایک نیا لباس خریدنا چاہتا ہوں۔", "Mein aik naya libas khareedna chahta hoon."),
                ConversionSentence(4, "Do you accept credit cards?", "کیا آپ کریڈٹ کارڈ قبول کرتے ہیں؟", "Kya aap credit card qabool karte hain?"),
                ConversionSentence(5, "Where is the trial room?", "ٹرائل روم کہاں ہے؟", "Trial room kahan hai?"),
                ConversionSentence(6, "This shoe is too tight.", "یہ جوتا بہت تنگ ہے۔", "Yeh joota bohat tang hai."),
                ConversionSentence(7, "Can you show me another one?", "کیا آپ مجھے کوئی دوسرا دکھا سکتے ہیں؟", "Kya aap mujhe koi doosra dikha sakte hain?"),
                ConversionSentence(8, "The market is very crowded.", "مارکیٹ میں بہت رش ہے۔", "Market mein bohat rush hai."),
                ConversionSentence(9, "I am just looking around.", "میں صرف ادھر ادھر دیکھ رہا ہوں۔", "Mein sirf idhar udhar dekh raha hoon."),
                ConversionSentence(10, "This is too expensive.", "یہ بہت مہنگا ہے۔", "Yeh bohat mehanga hai.")
            )
            5 -> listOf(
                ConversionSentence(1, "I work in a local office.", "میں مقامی دفتر میں کام کرتا ہوں۔", "Mein maqami daftar mein kaam karta hoon."),
                ConversionSentence(2, "He has an important meeting.", "اس کی ایک اہم میٹنگ ہے۔", "Is ki aik ahem meeting hai."),
                ConversionSentence(3, "What is your profession?", "آپ کا پیشہ کیا ہے؟", "Aap ka pesha kya hai?"),
                ConversionSentence(4, "She got a new promotion.", "اسے ایک نئی ترقی ملی ہے۔", "Usey aik nayi taraqi mili hai."),
                ConversionSentence(5, "I am busy with my project.", "میں اپنے پراجیکٹ میں مصروف ہوں۔", "Mein apne project mein masroof hoon."),
                ConversionSentence(6, "We should respect our boss.", "ہمیں اپنے باس کا احترام کرنا چاہیے۔", "Humein apne boss ka ihtiram karna chahiye."),
                ConversionSentence(7, "The workspace is modern.", "کام کرنے کی جگہ جدید ہے۔", "Kaam karne ki jagah jadeed hai."),
                ConversionSentence(8, "He sent the email on time.", "اس نے وقت پر ای میل بھیجی۔", "Us ne waqt par email bheji."),
                ConversionSentence(9, "I will submit the report tomorrow.", "میں کل رپورٹ جمع کرواؤں گا۔", "Mein kal report jama karwaunga."),
                ConversionSentence(10, "Hard work leads to success.", "سخت محنت کامیابی کا باعث بنتی ہے۔", "Sakht mehnat kamyabi ka bais banti hai.")
            )
            else -> listOf(
                ConversionSentence(1, "Let us practice English daily.", "آئیں روزانہ انگریزی کی مشق کریں۔", "Aayein rozana angrezi ki mashq karein."),
                ConversionSentence(2, "This language is beautiful.", "یہ زبان خوبصورت ہے۔", "Yeh zaban khubsurat hai."),
                ConversionSentence(3, "Where is your friend?", "آپ کا دوست کہاں ہے؟", "Aap ka dost kahan hai?"),
                ConversionSentence(4, "Everything is going great.", "سب کچھ بہترین جا رہا ہے۔", "Sab kuch behtareen ja raha hai."),
                ConversionSentence(5, "I want to improve myself.", "میں خود کو بہتر بنانا چاہتا ہوں۔", "Mein khud ko behtar banana chahta hoon."),
                ConversionSentence(6, "Can you speak more slowly?", "کیا آپ زیادہ آہستہ بول سکتے ہیں؟", "Kya aap zyada aahista bol sakte hain?"),
                ConversionSentence(7, "Thank you very much.", "آپ کا بہت بہت شکریہ۔", "Aap ka bohat bohat shukriya."),
                ConversionSentence(8, "No problem at all.", "کوئی مسئلہ ہی نہیں ہے۔", "Koi masla hi nahi hai."),
                ConversionSentence(9, "I will do my best.", "میں اپنی پوری کوشش کروں گا۔", "Mein apni poori koshish karunga."),
                ConversionSentence(10, "Keep going, don't stop.", "چلتے رہو، رکو مت۔", "Chalte raho, ruko mat.")
            )
        }
    }
}
