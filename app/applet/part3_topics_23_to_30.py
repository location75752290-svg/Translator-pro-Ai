# -*- coding: utf-8 -*-

def add_part3_23_to_30(topics):
    # 23. Hobbies & Free Time
    t23 = [
        ("What do you enjoy doing in your spare time?", "آپ فارغ اوقات میں کیا کرنا پسند کرتے ہیں؟", "Aap faarigh auqaat mein kya karna pasand kartay hain?"),
        ("I love tending to the flowers in my rooftop garden.", "مجھے چھت کے باغیچے میں پھولوں کی دیکھ بھال پسند ہے۔", "Mujhe chhat ke bagheeche mein phoolon ki dekh bhaal pasand hai."),
        ("Reading historical novels relaxes my mind after work.", "تاریخی ناول پڑھنا کام کے بعد میرے ذہن کو سکون دیتا ہے۔", "Tareekhi novel parhna kaam ke baad mere zehan ko sukoon deta hai."),
        ("Do you know how to play the acoustic guitar?", "کیا آپ کو ایکوسٹک گٹار بجانا آتا ہے؟", "Kya aap ko acoustic guitar bajana aata hai?"),
        ("I have recently taken up landscape oil painting.", "میں نے حال ہی میں تیل کے رنگوں سے مصوری شروع کی ہے۔", "Mein ne haal hi mein tail ke rangon se musawari shuru ki hai."),
        ("We often go hiking in the Margalla Hills on Sundays.", "ہم اتوار کو اکثر مارگلہ کی پہاڑیوں پر ہائیکنگ کے لیے جاتے ہیں۔", "Hum itwar ko aksar Margalla ki pahariyon par hiking ke liye jaatay hain."),
        ("I find solving complex chess puzzles very stimulating.", "شطرنج کے مشکل مسائل حل کرنا مجھے بہت پرجوش لگتا ہے۔", "Shatranj ke mushkil masail hal karna mujhe bohat purjosh lagta hai."),
        ("Cooking traditional dishes is my favorite creative outlet.", "روایتی کھانے پکانا میرا پسندیدہ تخلیقی مشغلہ ہے۔", "Riwayati khanay pakana mera pasandeeda takhleeqi mashghala hai."),
        ("Do you collect postal stamps or rare vintage coins?", "کیا آپ ڈاک کے ٹکٹ یا نایاب پرانے سکے جمع کرتے ہیں؟", "Kya aap daak ke ticket ya nayaab puranay sikkay jama kartay hain?"),
        ("Photography helps me capture beautiful fleeting moments.", "فوٹوگرافی مجھے خوبصورت گزرتے لمحوں کو قید کرنے میں مدد دیتی ہے۔", "Photography mujhe khubsurat guzartay lamhon ko qaid karne mein madad deti hai."),
        ("I enjoy cycling along the canal road early morning.", "مجھے صبح سویرے نہر کے کنارے سائیکل چلانا پسند ہے۔", "Mujhe subah sawayray nehar ke kinaray cycle chalana pasand hai."),
        ("Learning to bake sourdough bread has been exciting.", "خمیری روٹی بنانا سیکھنا بہت دلچسپ تجربہ رہا ہے۔", "Khameeri roti banana seekhna bohat dilchasp tajruba raha hai."),
        ("I write poetry in Urdu during quiet rainy evenings.", "میں بارش والی پرسکون شاموں میں اردو شاعری لکھتا ہوں۔", "Mein barish wali pursukoon shaamon mein Urdu shayari likhta hoon."),
        ("Calligraphy requires immense patience and steady hands.", "خطاطی کے لیے بے پناہ صبر اور سدھے ہوئے ہاتھوں کی ضرورت ہے۔", "Khattati ke liye be-panah sabr aur sudhay hue haathon ki zaroorat hai."),
        ("We play board games with the family every weekend.", "ہم ہر ویک اینڈ پر خاندان کے ساتھ بورڈ گیمز کھیلتے ہیں۔", "Hum har weekend par khandan ke sath board games kheltay hain."),
        ("Knitting warm woolen scarves is my grandmother's passion.", "اون کے گرم مفلر بننا میری دادی کا پسندیدہ کام ہے۔", "Oon ke garam muffler bunna meri dadi ka pasandeeda kaam hai."),
        ("I like listening to classical sufi music while relaxing.", "میں آرام کرتے وقت صوفیانہ کلام سننا پسند کرتا ہوں۔", "Mein aaram kartay waqt sufiana kalaam sunna pasand karta hoon."),
        ("Watching documentary films broadens my worldview.", "دستاویدی فلمیں دیکھنا میری معلومات اور سوچ میں اضافہ کرتا ہے۔", "Dastaweezi filmein dekhna meri maloomat aur soch mein izafa karta hai."),
        ("I am learning how to code mobile apps in my free time.", "میں فارغ وقت میں موبائل ایپس بنانا سیکھ رہا ہوں۔", "Mein faarigh waqt mein mobile apps banana seekh raha hoon."),
        ("Stargazing with a telescope is a magical experience.", "دوربین سے ستاروں کو دیکھنا ایک جادوئی تجربہ ہے۔", "Doorbeen se sitaron ko dekhna aik jadooi tajruba hai."),
        ("I love visiting art galleries and cultural museums.", "مجھے آرٹ گیلریوں اور ثقافتی عجائب گھروں کی سیر پسند ہے۔", "Mujhe art galleries aur saqafati ajaib gharon ki sair pasand hai."),
        ("Do you enjoy indoor games or outdoor physical sports?", "کیا آپ انڈور گیمز پسند کرتے ہیں یا بیرونی کھیل؟", "Kya aap indoor games pasand kartay hain ya bairooni khel?"),
        ("I practice meditation for fifteen minutes every evening.", "میں ہر شام پندرہ منٹ مراقبہ (میڈیٹیشن) کرتا ہوں۔", "Mein har shaam pandrah minute muraqba (meditation) karta hoon."),
        ("Birdwatching in the local wildlife sanctuary is peaceful.", "مقامی پارک میں پرندوں کا مشاہدہ کرنا بہت پرسکون ہے۔", "Maqami park mein parindon ka mushahida karna bohat pursukoon hai."),
        ("I love building intricate scale models of airplanes.", "مجھے ہوائی جہازوں کے چھوٹے ماڈل بنانا بہت پسند ہے۔", "Mujhe hawai jahazon ke chhotay model banana bohat pasand hai."),
        ("Pottery and sculpting clay help relieve stress.", "مٹی کے برتن اور مجسمے بنانا ذہنی دباؤ کو کم کرتا ہے۔", "Mitti ke bartan aur mujasmay banana zehni dabao ko kam karta hai."),
        ("I enjoy exploring vintage second-hand bookshops.", "مجھے پرانی کتابوں کی دکانوں کی خاک چھاننا پسند ہے۔", "Mujhe purani kitabon ki dukanon ki khaak chhanna pasand hai."),
        ("Learning conversational Spanish is my new hobby.", "ہسپانوی زبان بولنا سیکھنا میرا نیا مشغلہ ہے۔", "Spani zaban bolna seekhna mera naya mashghala hai."),
        ("I like organizing and editing our family holiday photos.", "مجھے چھٹیوں کی خاندانی تصاویر ترتیب دینا پسند ہے۔", "Mujhe chuttiyon ki khandani tasaveer tarteeb dena pasand hai."),
        ("Volunteering at the community animal shelter warms the heart.", "جانوروں کی پناہ گاہ میں رضاکارانہ خدمت دل کو خوشی دیتی ہے۔", "Janwaron ki panah-gah mein raza-karana khidmat dil ko khushi deti hai."),
        ("I enjoy writing a personal daily journal before sleep.", "مجھے سونے سے پہلے روزنامچہ (ڈائری) لکھنا پسند ہے۔", "Mujhe sonay se pehle roz-namcha (diary) likhna pasand hai."),
        ("We set up a small campfire during our mountain trek.", "ہم نے پہاڑی سفر کے دوران چھوٹا الاؤ جلایا۔", "Hum ne pahari safar ke dauran chhota alao jalaya."),
        ("Taking care of indoor houseplants brings nature indoors.", "گھر کے اندر پودوں کی دیکھ بھال ہریالی کا احساس دلاتی ہے۔", "Ghar ke andar paudon ki dekh bhaal haryali ka ehsas dilati hai."),
        ("I love solving Saturday morning crossword puzzles.", "مجھے ہفتے کی صبح اخبار کا معمہ حل کرنا پسند ہے۔", "Mujhe haftay ki subah akhbar ka muamma hal karna pasand hai."),
        ("Baking fresh chocolate chip cookies smells heavenly.", "چاکلیٹ بسکٹ پکانے کی خوشبو گھر کو مہکا دیتی ہے۔", "Chocolate biscuit pakanay ki khushboo ghar ko mehka deti hai."),
        ("I listen to informative history podcasts while commuting.", "میں سفر کے دوران تاریخی پوڈکاسٹ سنتا ہوں۔", "Mein safar ke dauran tareekhi podcast sunta hoon."),
        ("Making handcrafted greeting cards is very satisfying.", "اپنے ہاتھ سے عید کارڈ بنانا بہت لطف دیتا ہے۔", "Apne haath se Eid card banana bohat lutf deta hai."),
        ("I enjoy discovering independent coffee roasters in town.", "مجھے شہر کے نئے کافی کیفے تلاش کرنا پسند ہے۔", "Mujhe shehar ke naye coffee cafe talash karna pasand hai."),
        ("Practicing origami paper folding sharpens concentration.", "کاغذ سے مختلف اشکال بنانا توجہ کو تیز کرتا ہے۔", "Kaghaz se mukhtalif ashkaal banana tawajjo ko taiz karta hai."),
        ("I like watching theatrical plays and live drama.", "مجھے تھیٹر کے کھیل اور لائیو ڈرامے دیکھنا پسند ہے۔", "Mujhe theater ke khel aur live drame dekhna pasand hai."),
        ("Creating digital vector illustrations is my passion.", "ڈیجیٹل ویکٹر تصاویر بنانا میرا اصل شوق ہے۔", "Digital vector tasaveer banana mera asal shauq hai."),
        ("I love visiting local weekend farmer markets.", "مجھے ویک اینڈ کی سبزی اور پھلوں کی منڈی جانا پسند ہے۔", "Mujhe weekend ki sabzi aur phalon ki mandi jaana pasand hai."),
        ("Exploring drone aerial videography has been thrilling.", "ڈرون سے فضائی ویڈیوز بنانا انتہائی سنسنی خیز ہے۔", "Drone se fizai videos banana intehai sansani khez hai."),
        ("I spend hours listening to vintage vinyl audio records.", "میں پرانے گراموفون ریکارڈز سننے میں گھنٹوں گزارتا ہوں۔", "Mein puranay gramophone records sunnay mein ghanton guzarta hoon."),
        ("Taking evening nature strolls rejuvenates the soul.", "شام کو قدرت کی سیر روح کو تروتازہ کر دیتی ہے۔", "Shaam ko qudrat ki sair rooh ko tar-o-taaza kar deti hai."),
        ("I love practicing calligraphy with traditional bamboo pens.", "روایتی قلم سے خطاطی کی مشق کرنا میرا پسندیدہ کام ہے۔", "Riwayati qalam se khattati ki mashq karna mera pasandeeda kaam hai."),
        ("Collecting inspirational quotes motivates my weekly goals.", "حوصلہ افزا اقوال جمع کرنا میرے اہداف کو تقویت دیتا ہے۔", "Hausla-afza aqwaal jama karna mere ahdaaf ko taqwiyat deta hai."),
        ("I find peace in fishing by the quiet lakeside.", "پرسکون جھیل کے کنارے مچھلی پکڑنے میں مجھے سکون ملتا ہے۔", "Pursukoon jheel ke kinaray machhli pakarnay mein mujhe sukoon milta hai."),
        ("Having a creative hobby keeps your mind vibrant and young.", "ایک تخلیقی مشغلہ ذہن کو ہمیشہ تروتازہ اور چاق و چوبند رکھتا ہے۔", "Aik takhleeqi mashghala zehan ko hamesha tar-o-taaza rakhta hai."),
        ("Life is richer when you pursue your genuine passions.", "زندگی اس وقت زیادہ خوبصورت ہوتی ہے جب آپ اپنے شوق پورے کرتے ہیں۔", "Zindagi us waqt zyada khubsurat hoti hai jab aap apne shauq pooray kartay hain.")
    ]
    topics.append({
        "id": 23, "name": "Hobbies & Free Time", "urduName": "مشاغل اور تفریح", "icon": "sports_cricket",
        "desc": "Gardening, painting, cooking, and weekend leisure activities.", "sentences": t23
    })

    # Add topics 24 to 30
    import part3_topics_24_to_30
    part3_topics_24_to_30.add_24_to_30(topics)

print("Part 3 helper loaded.")
