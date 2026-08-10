package com.example.data.model

data class PracticeSentence(
    val english: String,
    val urdu: String,
    val exampleEn: String,
    val exampleUr: String
)

object PracticeSentences {
    val list = listOf(
        PracticeSentence(
            english = "Where are you going?",
            urdu = "آپ کہاں جا رہے ہیں؟",
            exampleEn = "Where is he going today?",
            exampleUr = "وہ آج کہاں جا رہا ہے؟"
        ),
        PracticeSentence(
            english = "I am going to the market.",
            urdu = "میں بازار جا رہا ہوں۔",
            exampleEn = "They are going to the school.",
            exampleUr = "وہ اسکول جا رہے ہیں۔"
        ),
        PracticeSentence(
            english = "What are you doing?",
            urdu = "آپ کیا کر رہے ہیں؟",
            exampleEn = "What is she doing now?",
            exampleUr = "وہ ابھی کیا کر رہی ہے؟"
        ),
        PracticeSentence(
            english = "I am reading a book.",
            urdu = "میں کتاب پڑھ رہا ہوں۔",
            exampleEn = "We are reading an interesting story.",
            exampleUr = "ہم ایک دلچسپ کہانی پڑھ رہے ہیں۔"
        ),
        PracticeSentence(
            english = "How are you today?",
            urdu = "آج آپ کیسے ہیں؟",
            exampleEn = "How is your father today?",
            exampleUr = "آج آپ کے والد کیسے ہیں؟"
        ),
        PracticeSentence(
            english = "I am feeling great.",
            urdu = "میں بہت اچھا محسوس کر رہا ہوں۔",
            exampleEn = "She is feeling much better now.",
            exampleUr = "وہ اب کافی بہتر محسوس کر رہی ہے۔"
        ),
        PracticeSentence(
            english = "Can you help me?",
            urdu = "کیا آپ میری مدد کر سکتے ہیں؟",
            exampleEn = "Can you help him with his work?",
            exampleUr = "کیا آپ کام میں اس کی مدد کر سکتے ہیں؟"
        ),
        PracticeSentence(
            english = "Yes, of course I can.",
            urdu = "جی ہاں، یقیناً میں کر سکتا ہوں۔",
            exampleEn = "Yes, of course we will come.",
            exampleUr = "جی ہاں، یقیناً ہم آئیں گے۔"
        ),
        PracticeSentence(
            english = "What is your name?",
            urdu = "آپ کا نام کیا ہے؟",
            exampleEn = "What is her brother's name?",
            exampleUr = "اس کے بھائی کا نام کیا ہے؟"
        ),
        PracticeSentence(
            english = "My name is Sarah.",
            urdu = "میرا نام سارہ ہے۔",
            exampleEn = "My friend's name is John.",
            exampleUr = "میرے دوست کا نام جان ہے۔"
        ),
        PracticeSentence(
            english = "Where do you live?",
            urdu = "آپ کہاں رہتے ہیں؟",
            exampleEn = "Where does your family live?",
            exampleUr = "آپ کا خاندان کہاں رہتا ہے؟"
        ),
        PracticeSentence(
            english = "I live in Lahore.",
            urdu = "میں لاہور میں رہتا ہوں۔",
            exampleEn = "He lives in a beautiful city.",
            exampleUr = "وہ ایک خوبصورت شہر میں رہتا ہے۔"
        ),
        PracticeSentence(
            english = "What time is it?",
            urdu = "کیا وقت ہوا ہے؟",
            exampleEn = "What time does the train arrive?",
            exampleUr = "ٹرین کس وقت پہنچتی ہے؟"
        ),
        PracticeSentence(
            english = "It is five o'clock.",
            urdu = "پانچ بجے ہیں۔",
            exampleEn = "It is almost midnight now.",
            exampleUr = "اب تقریباً آدھی رات ہو چکی ہے۔"
        ),
        PracticeSentence(
            english = "I like to drink tea.",
            urdu = "مجھے چائے پینا پسند ہے۔",
            exampleEn = "He likes to drink cold water.",
            exampleUr = "اسے ٹھنڈا پانی پینا پسند ہے۔"
        ),
        PracticeSentence(
            english = "She is a good doctor.",
            urdu = "وہ ایک اچھی ڈاکٹر ہے۔",
            exampleEn = "He is a very famous doctor in town.",
            exampleUr = "وہ شہر میں ایک بہت مشہور ڈاکٹر ہے۔"
        ),
        PracticeSentence(
            english = "The weather is very nice.",
            urdu = "موسم بہت اچھا ہے۔",
            exampleEn = "The weather was cold yesterday.",
            exampleUr = "کل موسم ٹھنڈا تھا۔"
        ),
        PracticeSentence(
            english = "We should start practicing.",
            urdu = "ہمیں مشق شروع کرنی چاہیے۔",
            exampleEn = "We should start our homework early.",
            exampleUr = "ہمیں اپنا ہوم ورک جلدی شروع کرنا چاہیے۔"
        ),
        PracticeSentence(
            english = "Learning English is fun.",
            urdu = "انگریزی سیکھنا مزے دار ہے۔",
            exampleEn = "Learning to paint is very creative.",
            exampleUr = "پینٹنگ سیکھنا بہت تخلیقی کام ہے۔"
        ),
        PracticeSentence(
            english = "He works in an office.",
            urdu = "وہ ایک دفتر میں کام کرتا ہے۔",
            exampleEn = "She works in a large hospital.",
            exampleUr = "وہ ایک بڑے ہسپتال میں کام کرتی ہے۔"
        ),
        PracticeSentence(
            english = "I am learning a new language.",
            urdu = "میں ایک نئی زبان سیکھ رہا ہوں۔",
            exampleEn = "They are learning French at school.",
            exampleUr = "وہ اسکول میں فرانسیسی سیکھ رہے ہیں۔"
        ),
        PracticeSentence(
            english = "Can I have some water, please?",
            urdu = "کیا مجھے تھوڑا پانی مل سکتا ہے؟",
            exampleEn = "Can I have some fresh juice, please?",
            exampleUr = "کیا مجھے تازہ جوس مل سکتا ہے، براہ کرم؟"
        ),
        PracticeSentence(
            english = "Please sit down here.",
            urdu = "براہ کرم یہاں بیٹھ جائیں۔",
            exampleEn = "Please stand up and answer.",
            exampleUr = "براہ کرم کھڑے ہو جائیں اور جواب دیں۔"
        ),
        PracticeSentence(
            english = "Thank you for your help.",
            urdu = "آپ کی مدد کا شکریہ۔",
            exampleEn = "Thank you for the delicious dinner.",
            exampleUr = "مزیدار رات کے کھانے کے لیے شکریہ۔"
        ),
        PracticeSentence(
            english = "Have a nice day ahead.",
            urdu = "آپ کا اگلا دن اچھا گزرے۔",
            exampleEn = "Have a safe journey to Islamabad.",
            exampleUr = "اسلام آباد کا سفر محفوظ ہو۔"
        ),
        PracticeSentence(
            english = "I am very busy today.",
            urdu = "میں آج بہت مصروف ہوں۔",
            exampleEn = "The doctor is very busy with patients.",
            exampleUr = "ڈاکٹر مریضوں کے ساتھ بہت مصروف ہے۔"
        ),
        PracticeSentence(
            english = "Let's meet tomorrow evening.",
            urdu = "آئیں کل شام ملتے ہیں۔",
            exampleEn = "Let's study together at the library.",
            exampleUr = "آئیں لائبریری میں مل کر پڑھتے ہیں۔"
        ),
        PracticeSentence(
            english = "This is my favorite book.",
            urdu = "یہ میری پسندیدہ کتاب ہے۔",
            exampleEn = "That is my favorite movie.",
            exampleUr = "وہ میری پسندیدہ فلم ہے۔"
        ),
        PracticeSentence(
            english = "I bought a new phone.",
            urdu = "میں نے ایک نیا فون خریدا۔",
            exampleEn = "She bought a new dress for Eid.",
            exampleUr = "اس نے عید کے لیے نیا لباس خریدا۔"
        ),
        PracticeSentence(
            english = "He speaks very fast.",
            urdu = "وہ بہت تیز بولتا ہے۔",
            exampleEn = "They talk very loudly in the class.",
            exampleUr = "وہ کلاس میں بہت اونچی آواز میں بات کرتے ہیں۔"
        ),
        PracticeSentence(
            english = "We are going on a vacation.",
            urdu = "ہم چھٹیوں پر جا رہے ہیں۔",
            exampleEn = "He is going on a business trip.",
            exampleUr = "وہ کاروباری سفر پر جا رہا ہے۔"
        ),
        PracticeSentence(
            english = "I need to sleep now.",
            urdu = "مجھے اب سونے کی ضرورت ہے۔",
            exampleEn = "You need to rest after the long walk.",
            exampleUr = "آپ کو لمبی چہل قدمی کے بعد آرام کی ضرورت ہے۔"
        ),
        PracticeSentence(
            english = "It is raining outside.",
            urdu = "باہر بارش ہو رہی ہے۔",
            exampleEn = "It was snowing in Murree.",
            exampleUr = "مری میں برف باری ہو رہی تھی۔"
        ),
        PracticeSentence(
            english = "Where is the nearest bank?",
            urdu = "قریب ترین بینک کہاں ہے؟",
            exampleEn = "Where is the nearest bus stop?",
            exampleUr = "قریب ترین بس سٹاپ کہاں ہے؟"
        ),
        PracticeSentence(
            english = "I forgot my keys today.",
            urdu = "میں آج اپنی چابیاں بھول گیا۔",
            exampleEn = "She forgot her purse in the car.",
            exampleUr = "وہ گاڑی میں اپنا پرس بھول گئی۔"
        ),
        PracticeSentence(
            english = "She sings beautifully.",
            urdu = "وہ بہت خوبصورت گاتی ہے۔",
            exampleEn = "The birds are chirping beautifully.",
            exampleUr = "پرندے خوبصورتی سے چہچہا رہے ہیں۔"
        ),
        PracticeSentence(
            english = "Let's play some cricket.",
            urdu = "آئیں تھوڑی کرکٹ کھیلتے ہیں۔",
            exampleEn = "Let's play chess in the room.",
            exampleUr = "آئیں کمرے میں شطرنج کھیلتے ہیں۔"
        ),
        PracticeSentence(
            english = "The food is delicious.",
            urdu = "کھانا بہت لذیذ ہے۔",
            exampleEn = "The mangoes are very sweet and delicious.",
            exampleUr = "آم بہت میٹھے اور لذیذ ہیں۔"
        ),
        PracticeSentence(
            english = "I will call you later.",
            urdu = "میں آپ کو بعد میں فون کروں گا۔",
            exampleEn = "She will write a letter later.",
            exampleUr = "وہ بعد میں خط لکھے گی۔"
        ),
        PracticeSentence(
            english = "Keep up the good work.",
            urdu = "اچھا کام جاری رکھیں۔",
            exampleEn = "Keep up your fitness routine.",
            exampleUr = "اپنے فٹنس معمولات کو برقرار رکھیں۔"
        ),
        PracticeSentence(
            english = "What is the price of this?",
            urdu = "اس کی قیمت کیا ہے؟",
            exampleEn = "What is the price of these shoes?",
            exampleUr = "ان جوتوں کی قیمت کیا ہے؟"
        ),
        PracticeSentence(
            english = "Can I pay by card?",
            urdu = "کیا میں کارڈ کے ذریعے ادائیگی کر سکتا ہوں؟",
            exampleEn = "Can I pay with cash here?",
            exampleUr = "کیا میں یہاں نقد رقم سے ادائیگی کر سکتا ہوں؟"
        ),
        PracticeSentence(
            english = "This shop has great discounts.",
            urdu = "اس دکان پر بہترین رعایتیں ہیں۔",
            exampleEn = "That market has cheap clothes.",
            exampleUr = "اس بازار میں سستے کپڑے ہیں۔"
        ),
        PracticeSentence(
            english = "I want to travel the world.",
            urdu = "میں دنیا کا سفر کرنا چاہتا ہوں۔",
            exampleEn = "She wants to visit historical places.",
            exampleUr = "وہ تاریخی مقامات کی سیر کرنا چاہتی ہے۔"
        ),
        PracticeSentence(
            english = "We should protect our environment.",
            urdu = "ہمیں اپنے ماحول کی حفاظت کرنی چاہیے۔",
            exampleEn = "We should plant more green trees.",
            exampleUr = "ہمیں زیادہ سبز درخت لگانے چاہئیں۔"
        ),
        PracticeSentence(
            english = "Healthy food makes you strong.",
            urdu = "صحت بخش کھانا آپ کو مضبوط بناتا ہے۔",
            exampleEn = "Daily exercise keeps you fit.",
            exampleUr = "روزانہ کی ورزش آپ کو تندرست رکھتی ہے۔"
        ),
        PracticeSentence(
            english = "Can you repeat that, please?",
            urdu = "کیا آپ اسے دہرائیں گے، براہ کرم؟",
            exampleEn = "Can you show me the way, please?",
            exampleUr = "کیا آپ مجھے راستہ دکھا سکتے ہیں، براہ کرم؟"
        ),
        PracticeSentence(
            english = "I don't understand this word.",
            urdu = "میں اس لفظ کو نہیں سمجھا۔",
            exampleEn = "He doesn't understand Urdu grammar.",
            exampleUr = "وہ اردو گرامر نہیں سمجھتا۔"
        ),
        PracticeSentence(
            english = "Everything will be fine.",
            urdu = "سب ٹھیک ہو جائے گا۔",
            exampleEn = "Don't worry, everything is perfect.",
            exampleUr = "فکر نہ کریں، سب کچھ بالکل ٹھیک ہے۔"
        ),
        PracticeSentence(
            english = "Practice makes a man perfect.",
            urdu = "مشق انسان کو کامل بناتی ہے۔",
            exampleEn = "Reading daily improves your knowledge.",
            exampleUr = "روزانہ پڑھنا آپ کی معلومات میں اضافہ کرتا ہے۔"
        )
    )
}
