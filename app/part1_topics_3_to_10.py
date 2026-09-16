# -*- coding: utf-8 -*-

def add_remaining_topics(topics):
    # 3. Food & Restaurant (50 sentences)
    t3 = []
    t3.extend([
        ("Can I see the food and drinks menu, please?", "کیا مجھے برائے مہربانی مینو مل سکتا ہے؟", "Kya mujhe baraye meharbani menu mil sakta hai?"),
        ("What is today's special dish?", "آج کی خاص ڈش کون سی ہے؟", "Aaj ki khaas dish kaun si hai?"),
        ("I would like to order chicken biryani.", "میں چکن بریانی کا آرڈر دینا چاہوں گا۔", "Mein chicken biryani ka order dena chahunga."),
        ("Please make the food less spicy.", "براہ کرم کھانا کم مصالحے دار بنائیے گا۔", "Barah-e-karam khana kam masalay-daar banayiye ga."),
        ("Could you bring a bottle of mineral water?", "کیا آپ منرل واٹر کی ایک بوتل لا سکتے ہیں؟", "Kya aap mineral water ki aik botal laa saktay hain?"),
        ("The mutton curry was absolutely delicious.", "مٹن کا سالن واقعی انتہائی لذیذ تھا۔", "Mutton ka salan waqai intehai lazeez tha."),
        ("May we have the bill, please?", "کیا ہمیں برائے مہربانی بل مل سکتا ہے؟", "Kya humein baraye meharbani bill mil sakta hai?"),
        ("Do you accept card payments or only cash?", "کیا آپ کارڈ قبول کرتے ہیں یا صرف نقد رقم؟", "Kya aap card qabool kartay hain ya sirf naqd raqam?"),
        ("I am allergic to peanuts and seafood.", "مجھے مونگ پھلی اور سمندری خوراک سے الرجی ہے۔", "Mujhe moong phalli aur samandari khorak se allergy hai."),
        ("Please pack the remaining food for takeout.", "براہ کرم بچا ہوا کھانا پارسل کر دیں۔", "Barah-e-karam bacha hua khana parcel kar dein."),
        ("Could we get a table for four people?", "کیا ہمیں چار افراد کے لیے میز مل سکتی ہے؟", "Kya humein chaar afraad ke liye maiz mil sakti hai?"),
        ("Do you have any vegetarian options available?", "کیا آپ کے پاس سبزی خوروں کے لیے کھانے ہیں؟", "Kya aap ke paas sabzi khoron ke liye khanay hain?"),
        ("How long will our order take to prepare?", "ہمارا آرڈر تیار ہونے میں کتنا وقت لگے گا؟", "Hamara order tayar honay mein kitna waqt lagay ga?"),
        ("Could you bring extra napkins and glasses?", "کیا آپ اضافی ٹشو پیپرز اور گلاس لا سکتے ہیں؟", "Kya aap izafi tissue papers aur glass laa saktay hain?"),
        ("This soup is piping hot and flavorful.", "یہ سوپ بہت گرم اور ذائقہ دار ہے۔", "Yeh soup bohat garam aur zaiqay-daar hai."),
        ("I prefer my steak well done.", "میں اچھی طرح پکا ہوا گوشت پسند کرتا ہوں۔", "Mein achi tarah paka hua gosht pasand karta hoon."),
        ("Would you like any dessert after your meal?", "کیا آپ کھانے کے بعد میٹھا لینا پسند کریں گے؟", "Kya aap khanay ke baad meetha lena pasand karein ge?"),
        ("I will have traditional kheer for dessert.", "میں میٹھے میں روایتی کھیر لوں گا۔", "Mein meethay mein riwayati kheer loonga."),
        ("Is service tax included in the total bill?", "کیا کل بل میں سروس ٹیکس شامل ہے؟", "Kya kul bill mein service tax shamil hai?"),
        ("The ambiance in this restaurant is lovely.", "اس ریستوراں کا ماحول بہت پرسکون اور خوبصورت ہے۔", "Is restaurant ka mahaul bohat pursukoon aur khubsurat hai."),
        ("Can we reserve an outdoor terrace table?", "کیا ہم باہر چھت پر میز بک کروا سکتے ہیں؟", "Kya hum bahar chhat par maiz book karwa saktay hain?"),
        ("Do you serve freshly squeezed fruit juices?", "کیا آپ تازہ نکالے ہوئے پھلوں کے جوس پیش کرتے ہیں؟", "Kya aap taaza nikaalay hue phalon ke juice pesh kartay hain?"),
        ("Please bring the salad with dressing on the side.", "سلاد کے ساتھ ڈریسنگ الگ سے لائیں۔", "Salad ke sath dressing alag se laayein."),
        ("This chicken handi tastes authentic and fresh.", "اس چکن ہانڈی کا ذائقہ بالکل اصلی اور تازہ ہے۔", "Is chicken handi ka zaiqa bilkul asli aur taaza hai."),
        ("Could you please warm this dish up again?", "کیا آپ اس کھانے کو دوبارہ گرم کر سکتے ہیں؟", "Kya aap is khanay ko dobara garam kar saktay hain?"),
        ("We would like to give our compliments to the chef.", "ہم شیف کی بہترین کوکنگ کی تعریف کرنا چاہتے ہیں۔", "Hum chef ki behtareen cooking ki tareef karna chahtay hain."),
        ("Can I get a cup of hot green tea?", "کیا مجھے گرم سبز چائے (قہوہ) مل سکتا ہے؟", "Kya mujhe garam sabz chaye (qahwa) mil sakta hai?"),
        ("Is this dish spicy or mild in taste?", "کیا یہ ڈش زیادہ تیز ہے یا ہلکی؟", "Kya yeh dish zyada taiz hai ya halki?"),
        ("Please do not add any onions or garlic.", "براہ کرم پیاز یا لہسن شامل مت کیجئے گا۔", "Barah-e-karam pyaaz ya lehsan shamil mat kijiye ga."),
        ("Keep the change, thank you for the service.", "باقی کھلے پیسے آپ رکھ لیں، خدمت کا شکریہ۔", "Baaqi khulay paisay aap rakh lein, khidmat ka shukriya."),
        ("Where is the hand washing area located?", "ہاتھ دھونے کی جگہ کس طرف ہے؟", "Haath dhonay ki jagah kis taraf hai?"),
        ("Breakfast is served until eleven in the morning.", "صبح کا ناشتہ گیارہ بجے تک پیش کیا جاتا ہے۔", "Subah ka nashta gyarah bajay tak pesh kiya jata hai."),
        ("I crave crispy garlic naan and seekh kabab.", "میرا دل کرسپی گارلک نان اور سیخ کباب کھانے کو کر رہا ہے۔", "Mera dil crispy garlic naan aur seekh kabab khanay ko kar raha hai."),
        ("Could we get another fork and spoon here?", "کیا ہمیں ایک اور کانٹا اور چمچ مل سکتا ہے؟", "Kya humein aik aur kaanta aur chamach mil sakta hai?"),
        ("The portion size is generous enough for two.", "یہ خوراک دو افراد کے لیے کافی ہے۔", "Yeh khorak do afraad ke liye kaafi hai."),
        ("Let's try the seafood platter today.", "آئیں آج سی فوڈ پلیٹر ٹرائی کرتے ہیں۔", "Aayein aaj seafood platter try kartay hain."),
        ("Is halal food served in this restaurant?", "کیا اس ریستوراں میں حلال کھانا پیش کیا جاتا ہے؟", "Kya is restaurant mein halal khana pesh kiya jata hai?"),
        ("Yes, all meat is certified 100% halal.", "جی ہاں، تمام گوشت سو فیصد حلال ہے۔", "Jee haan, tamam gosht sau feesad halal hai."),
        ("Can I get extra mint sauce and yogurt?", "کیا مجھے اضافی پودینہ چٹنی اور رائتہ مل سکتا ہے؟", "Kya mujhe izafi podeena chutney aur raita mil sakta hai?"),
        ("We are celebrating our friend's birthday today.", "ہم آج اپنے دوست کی سالگرہ منا رہے ہیں۔", "Hum aaj apne dost ki salgirah mana rahe hain."),
        ("Could you bring a birthday cake with candles?", "کیا آپ موم بتیوں کے ساتھ کیک لا سکتے ہیں؟", "Kya aap mom batiyon ke sath cake laa saktay hain?"),
        ("This mango lassi is sweet and refreshing.", "یہ مینگو لسی میٹھی اور تازگی بخش ہے۔", "Yeh mango lassi meethi aur taazgi bakhsh hai."),
        ("I am completely full, I cannot eat another bite.", "میرا پیٹ بالکل بھر چکا ہے، میں مزید نہیں کھا سکتا۔", "Mera pait bilkul bhar chuka hai, mein mazeed nahi khaa sakta."),
        ("The service was quick, courteous, and polite.", "خدمات تیز رفتار، شائستہ اور بہترین تھیں۔", "Khidmaat taiz raftaar, shaista aur behtareen theen."),
        ("Do you offer home delivery service?", "کیا آپ ہوم ڈلیوری کی سروس فراہم کرتے ہیں؟", "Kya aap home delivery ki service faraham kartay hain?"),
        ("You can order online through our food app.", "آپ ہماری فوڈ ایپ کے ذریعے آن لائن آرڈر کر سکتے ہیں۔", "Aap hamari food app ke zariye online order kar saktay hain."),
        ("We had a delightful family dinner tonight.", "آج رات ہمارا خاندانی کھانا بہت شاندار رہا۔", "Aaj raat hamara khandani khana bohat shandar raha."),
        ("Please bring toothpicks and mint to the table.", "میز پر خلال اور پودینے کی گولیاں لا دیں۔", "Maiz par khilal aur podeenay ki goliyan laa dein."),
        ("I will definitely recommend this diner to friends.", "میں دوستوں کو اس ریستوراں کی ضرور سفارش کروں گا۔", "Mein doston ko is restaurant ki zaroor sifarish karunga."),
        ("Thank you for the delicious meal!", "لذیذ کھانے کا بہت بہت شکریہ!", "Lazeez khanay ka bohat bohat shukriya!")
    ])
    assert len(t3) == 50
    topics.append({
        "id": 3, "name": "Food & Restaurant", "urduName": "کھانا اور ریستوراں", "icon": "restaurant",
        "desc": "Ordering food, reserving tables, and food preferences.", "sentences": t3
    })

    # 4. Shopping & Store (50 sentences)
    t4 = []
    t4.extend([
        ("How much does this pair of shoes cost?", "جوتے کے اس جوڑے کی کیا قیمت ہے؟", "Jootay ke is jorray ki kya qeemat hai?"),
        ("Can you give me a reasonable discount?", "کیا آپ مجھے مناسب رعایت دے سکتے ہیں؟", "Kya aap mujhe munasib riayat de saktay hain?"),
        ("Where can I find the fitting room?", "فٹنگ روم کس طرف ہے؟", "Fitting room kis taraf hai?"),
        ("This shirt is too tight, do you have a larger size?", "یہ قمیض تنگ ہے، کیا آپ کے پاس بڑا سائز ہے؟", "Yeh qameez tang hai, kya aap ke paas barra size hai?"),
        ("I am just looking around for now, thank you.", "میں ابھی صرف دیکھ رہا ہوں، شکریہ۔", "Mein abhi sirf dekh raha hoon, shukriya."),
        ("Do you have this design in black or navy blue?", "کیا آپ کے پاس یہ ڈیزائن کالے یا نیلے رنگ میں ہے؟", "Kya aap ke paas yeh design kaalay ya neelay rang mein hai?"),
        ("Is there any warranty on this electronic item?", "کیا اس الیکٹرانک چیز پر کوئی وارنٹی ہے؟", "Kya is electronic cheez par koi warranty hai?"),
        ("Can I exchange this if the size doesn't fit?", "اگر سائز پورا نہ آئے تو کیا میں تبدیل کروا سکتا ہوں؟", "Agar size poora na aaye to kya mein tabdeel karwa sakta hoon?"),
        ("Please give me the printed receipt.", "براہ کرم مجھے پرنٹ شدہ رسید دیں۔", "Barah-e-karam mujhe printed raseed dein."),
        ("These mangoes are very fresh and sweet.", "یہ آم بہت تازہ اور میٹھے ہیں۔", "Yeh aam bohat taaza aur meethay hain."),
        ("Are these goods on sale or regular price?", "کیا یہ اشیاء سیل پر ہیں یا اصل قیمت پر؟", "Kya yeh ashya sale par hain ya asal qeemat par?"),
        ("Where is the grocery and dairy section?", "کریانہ اور دودھ دہی کا سیکشن کہاں ہے؟", "Kiryana aur doodh dahi ka section kahan hai?"),
        ("Do you have pure cotton fabrics available?", "کیا آپ کے پاس خالص سوتی کپڑے دستیاب ہیں؟", "Kya aap ke paas khalis sooti kapray dastiyab hain?"),
        ("This price seems a bit too high for this item.", "اس چیز کی قیمت کچھ زیادہ معلوم ہوتی ہے۔", "Is cheez ki qeemat kuch zyada maloom hoti hai."),
        ("Can I pay using JazzCash or EasyPaisa?", "کیا میں جاز کیش یا ایزی پیسہ سے ادائیگی کر سکتا ہوں؟", "Kya mein JazzCash ya EasyPaisa se adayigi kar sakta hoon?"),
        ("Do you have a shopping cart or hand basket?", "کیا آپ کے پاس شاپنگ ٹرالی یا ٹوکری ہے؟", "Kya aap ke paas shopping trolley ya tokri hai?"),
        ("What time does this supermarket close tonight?", "یہ سپر مارکیٹ آج رات کس وقت بند ہوتی ہے؟", "Yeh super market aaj raat kis waqt band hoti hai?"),
        ("I would like to return this defective item.", "میں یہ خراب چیز واپس کرنا چاہتا ہوں۔", "Mein yeh kharab cheez wapas karna chahta hoon."),
        ("Please wrap this perfume box as a gift.", "اس پرفیوم کے ڈبے کو گفٹ پیک کر دیں۔", "Is perfume ke dabbay ko gift pack kar dein."),
        ("Is this leather genuine or artificial?", "کیا یہ چمڑا اصلی ہے یا مصنوعی؟", "Kya yeh chamrra asli hai ya masnooi?"),
        ("Do you sell winter coats and wool jackets?", "کیا آپ سردیوں کے کوٹ اور گرم جیکٹس بیچتے ہیں؟", "Kya aap sardiyon ke coat aur garam jackets bechtay hain?"),
        ("Where can I find organic fresh vegetables?", "مجھے تازہ اور نامیاتی سبزیاں کہاں ملیں گی؟", "Mujhe taaza aur namiyati sabziyan kahan milein gi?"),
        ("This dress fits me perfectly and looks elegant.", "یہ لباس مجھ پر بالکل پورا اور خوبصورت لگتا ہے۔", "Yeh libaas mujh par bilkul poora aur khubsurat lagta hai."),
        ("Do you provide free home delivery on big orders?", "کیا آپ بڑے آرڈرز پر مفت ہوم ڈلیوری دیتے ہیں؟", "Kya aap baray orders par muft home delivery detay hain?"),
        ("Where is the cash payment counter?", "کیش ادائیگی کا کاؤنٹر کس طرف ہے؟", "Cash adayigi ka counter kis taraf hai?"),
        ("Is there a buy-one-get-one-free offer today?", "کیا آج ایک کے ساتھ ایک مفت والی پیشکش ہے؟", "Kya aaj aik ke sath aik muft wali peshkash hai?"),
        ("These imported chocolates are very delicious.", "یہ امپورٹڈ چاکلیٹس بہت لذیذ ہیں۔", "Yeh imported chocolates bohat lazeez hain."),
        ("I need two kilograms of basmati rice.", "مجھے دو کلو باسمتی چاول چاہئیں۔", "Mujhe do kilo basmati chawal chahiyen."),
        ("Can you slice this loaf of bread for me?", "کیا آپ اس ڈبل روٹی کے ٹکڑے کر سکتے ہیں؟", "Kya aap is double roti ke tukray kar saktay hain?"),
        ("We offer special festive discounts on Eid.", "ہم عید پر خصوصی تہواری رعایت دیتے ہیں۔", "Hum Eid par khusoosi tehwaari riayat detay hain."),
        ("Where can I find school supplies and notebooks?", "اسکول کی اسٹیشنری اور کاپیاں کہاں ہیں؟", "School ki stationery aur copyan kahan hain?"),
        ("This watch comes with a one-year international warranty.", "اس گھڑی کی ایک سال کی بین الاقوامی وارنٹی ہے۔", "Is gharri ki aik saal ki bain-ul-aqwami warranty hai."),
        ("Do you have running sneakers in size nine?", "کیا آپ کے پاس نو نمبر کا اسپورٹس جوتا ہے؟", "Kya aap ke paas nau number ka sports joota hai?"),
        ("I will take two packs of green tea bags.", "میں سبز چائے کے دو پیکٹ لوں گا۔", "Mein sabz chaye ke do packet loonga."),
        ("Please handle these fragile glass items carefully.", "ان شیشے کی نازک چیزوں کو احتیاط سے سنبھالیں۔", "In sheeshay ki naazuk cheezon ko ahtiyat se sambhalein."),
        ("Is there any discount if I purchase in bulk?", "اگر میں زیادہ مقدار میں خریدوں تو کیا رعایت ملے گی؟", "Agar mein zyada miqdaar mein khareedoon to kya riayat milay gi?"),
        ("This mirror finish is scratch resistant.", "اس کا شیشہ خراشوں سے محفوظ ہے۔", "Is ka sheesha kharaashon se mahfooz hai."),
        ("Can I try this sunglasses frame on?", "کیا میں یہ دھوپ کا چشمہ پہن کر دیکھ سکتا ہوں؟", "Kya mein yeh dhoop ka chashma pehan kar dekh sakta hoon?"),
        ("Our shop is open seven days a week.", "ہماری دکان ہفتے کے ساتوں دن کھلی رہتی ہے۔", "Hamari dukan haftay ke saaton din khuli rehti hai."),
        ("Please put these groceries in an eco-friendly paper bag.", "یہ سودا سلف کاغذی تھیلے میں ڈال دیں۔", "Yeh sauda salaf kaghazi thelay mein daal dein."),
        ("Do you sell mobile phone screen protectors?", "کیا آپ موبائل کے اسکرین پروٹیکٹر بیچتے ہیں؟", "Kya aap mobile ke screen protector bechtay hain?"),
        ("The quality of this fabric is premium and durable.", "اس کپڑے کا معیار بہترین اور پائیدار ہے۔", "Is kapray ka meyaar behtareen aur payedaar hai."),
        ("I need a reliable power bank for traveling.", "مجھے سفر کے لیے ایک اچھا پاور بینک چاہیے۔", "Mujhe safar ke liye aik acha power bank chahiye."),
        ("Can you test this electric kettle before packing?", "کیا آپ پیک کرنے سے پہلے یہ کیتلی چیک کر سکتے ہیں؟", "Kya aap pack karne se pehle yeh kaitli check kar saktay hain?"),
        ("Here is the exact cash amount.", "یہ لیں بالکل پورے پیسے ہیں۔", "Yeh lein bilkul pooray paisay hain."),
        ("Thank you for shopping at our department store.", "ہمارے اسٹور سے خریداری کا بہت شکریہ۔", "Hamare store se khareedari ka bohat shukriya."),
        ("Please keep the bill safe for return policy.", "واپسی کے لیے بل سنبھال کر رکھیے گا۔", "Wapasi ke liye bill sambhal kar rakhiye ga."),
        ("Are these items locally manufactured?", "کیا یہ چیزیں مقامی طور پر بنی ہوئی ہیں؟", "Kya yeh cheezon maqami tor par bani hui hain?"),
        ("We support local artisans and small industries.", "ہم مقامی کاریگروں اور صنعت کی حوصلہ افزائی کرتے ہیں۔", "Hum maqami kareegaron aur sana'at ki hausla afzayi kartay hain."),
        ("Have a wonderful day and visit us again!", "آپ کا دن اچھا گزرے، دوبارہ ضرور تشریف لائیں!", "Aap ka din acha guzray, dobara zaroor tashreef laayein!")
    ])
    assert len(t4) == 50
    topics.append({
        "id": 4, "name": "Shopping & Store", "urduName": "خریداری اور دکان", "icon": "shopping_bag",
        "desc": "Inquiring about prices, bargaining, and buying goods.", "sentences": t4
    })

    # Add topics 5 to 10 via helper
    import part1_topics_5_to_10
    part1_topics_5_to_10.add_5_to_10(topics)

print("Part 1 3-10 helper loaded.")
