# -*- coding: utf-8 -*-

def add_7_to_10(topics):
    # 7. Travel & Airport
    t7 = [
        ("Where is the check-in counter for flight PK-302?", "فلائٹ پی کے 302 کا چیک ان کاؤنٹر کہاں ہے؟", "Flight PK-302 ka check-in counter kahan hai?"),
        ("May I see your passport and boarding pass?", "کیا میں آپ کا پاسپورٹ اور بورڈنگ پاس دیکھ سکتا ہوں؟", "Kya mein aap ka passport aur boarding pass dekh sakta hoon?"),
        ("How many bags are you checking in today?", "آج آپ کے پاس جمع کروانے کے لیے کتنے بیگز ہیں؟", "Aaj aap ke paas jama karwane ke liye kitnay bags hain?"),
        ("Is my flight on schedule or delayed?", "کیا میری پرواز وقت پر ہے یا تاخیر کا شکار ہے؟", "Kya meri parwaaz waqt par hai ya taakheer ka shikar hai?"),
        ("Where is gate number fourteen located?", "گیٹ نمبر چودہ کس طرف واقع ہے؟", "Gate number chodah kis taraf waqia hai?"),
        ("Please place all metal objects in the security tray.", "تمام دھاتی اشیاء سیکیورٹی ٹرے میں رکھیں۔", "Tamam dhaati ashya security tray mein rakhein."),
        ("I have nothing illegal to declare at customs.", "میرے پاس کسٹمز میں ڈکلیئر کرنے کے لیے کوئی غیر قانونی چیز نہیں۔", "Mere paas customs mein declare karne ke liye koi ghair qanooni cheez nahi."),
        ("Where is the baggage claim carousel for arrivals?", "آمد کے لیے سامان حاصل کرنے کا بیلٹ کہاں ہے؟", "Aamad ke liye saman haasil karne ka belt kahan hai?"),
        ("My luggage appears to be delayed or misplaced.", "لگتا ہے میرا سامان تاخیر کا شکار یا گم ہو گیا ہے۔", "Lagta hai mera saman taakheer ka shikar ya gum ho gaya hai."),
        ("Have a safe and pleasant flight!", "آپ کی پرواز محفوظ اور خوشگوار ہو!", "Aap ki parwaaz mahfooz aur khushgawar ho!"),
        ("Do you prefer a window seat or an aisle seat?", "کیا آپ کھڑکی والی سیٹ پسند کریں گے یا راستے والی؟", "Kya aap khirki wali seat pasand karein ge ya rastay wali?"),
        ("I would love to have a window seat, please.", "مجھے کھڑکی کے پاس والی سیٹ دے دیں، شکریہ۔", "Mujhe khirki ke paas wali seat de dein, shukriya."),
        ("What is the maximum baggage weight allowance per passenger?", "فی مسافر سامان کا زیادہ سے زیادہ وزن کتنا ہو سکتا ہے؟", "Fi musafir saman ka zyada se zyada wazan kitna ho sakta hai?"),
        ("Each passenger is allowed thirty kilograms of check-in luggage.", "ہر مسافر کو تیس کلو تک کا سامان لے جانے کی اجازت ہے۔", "Har musafir ko tees kilo tak ka saman le jaane ki ijazat hai."),
        ("Is there free Wi-Fi access in the departure terminal?", "کیا ڈیپارچر ٹرمینل میں مفت وائی فائی کی سہولت ہے؟", "Kya departure terminal mein muft Wi-Fi ki sahulat hai?"),
        ("Please fasten your seatbelts during takeoff and landing.", "جہاز کے اڑان بھرنے اور اترنے کے دوران سیٹ بیلٹ باندھ لیں۔", "Jahaz ke uraan bharnay aur utarnay ke dauran seat belt baandh lein."),
        ("The flight attendant will serve dinner shortly.", "فلائٹ اٹینڈنٹ تھوڑی دیر میں کھانا پیش کریں گی۔", "Flight attendant thori der mein khana pesh karein gi."),
        ("Can I get an extra blanket and earphones?", "کیا مجھے اضافی کمبل اور ہیڈ فون مل سکتے ہیں؟", "Kya mujhe izafi kambal aur headphones mil saktay hain?"),
        ("We are experiencing slight weather turbulence right now.", "اس وقت جہاز ہلکے سے جھٹکے محسوس کر رہا ہے۔", "Is waqt jahaz halkay se jhatkay mehsoos kar raha hai."),
        ("Please remain seated until the seatbelt sign turns off.", "جب تک سیٹ بیلٹ کا نشان بند نہ ہو، بیٹھے رہیں۔", "Jab tak seat belt ka nishan band na ho, baithay rahein."),
        ("Where can I exchange foreign currency at the airport?", "ایئرپورٹ پر غیر ملکی کرنسی کہاں تبدیل کی جا سکتی ہے؟", "Airport par ghair mulki currency kahan tabdeel ki ja sakti hai?"),
        ("There is a currency exchange booth near terminal exit.", "ٹرمینل کے باہر نکلنے والے راستے کے پاس منی ایکسچینج ہے۔", "Terminal ke bahar nikaltay rastay ke paas money exchange hai."),
        ("Where can I catch an airport taxi or cab?", "ایئرپورٹ ٹیکسی کہاں سے مل سکتی ہے؟", "Airport taxi kahan se mil sakti hai?"),
        ("Follow the yellow overhead signs to the taxi rank.", "ٹیکسی اسٹینڈ کے لیے پیلے تیر کے نشانات کے پیچھے جائیں۔", "Taxi stand ke liye peelay teer ke nishanaat ke peechay jayein."),
        ("How long is our scheduled layover in Dubai?", "دبئی میں ہمارا کتنا طویل قیام (اسٹاپ اوور) ہے؟", "Dubai mein hamara kitna taweel qiyam (stopover) hai?"),
        ("The transit layover is approximately three hours.", "ٹرانزٹ کا وقت تقریباً تین گھنٹے ہے۔", "Transit ka waqt taqreeban teen ghantay hai."),
        ("Do I need a transit visa for this connection?", "کیا اس کنکشن کے لیے ٹرانزٹ ویزا درکار ہے؟", "Kya is connection ke liye transit visa darkaar hai?"),
        ("No transit visa is required if staying inside airport.", "اگر آپ ایئرپورٹ کے اندر رہیں تو ویزا کی ضرورت نہیں۔", "Agar aap airport ke andar rahein to visa ki zaroorat nahi."),
        ("Please fill out the international arrival declaration form.", "براہ کرم بین الاقوامی آمد کا ڈیکلیریشن فارم پُر کریں۔", "Barah-e-karam bain-ul-aqwami aamad ka declaration form pur karein."),
        ("What is the purpose of your visit to our country?", "ہمارے ملک آنے کا آپ کا بنیادی مقصد کیا ہے؟", "Hamare mulk aanay ka aap ka bunyadi maqsad kya hai?"),
        ("I am visiting for tourism and sightseeing with family.", "میں اپنے اہل خانہ کے ساتھ سیر و تفریح کے لیے آیا ہوں۔", "Mein apne ahl-e-khana ke sath sair-o-tafreeh ke liye aaya hoon."),
        ("How many days do you intend to stay here?", "آپ کا یہاں کتنے دن قیام کرنے کا ارادہ ہے؟", "Aap ka yahan kitnay din qiyam karne ka irada hai?"),
        ("I will stay for exactly ten days before returning.", "میں واپسی سے قبل ٹھیک دس دن قیام کروں گا۔", "Mein wapasi se qabal theek das din qiyam karunga."),
        ("Here is my confirmed return flight ticket.", "یہ میرا تصدیق شدہ واپسی کا ہوائی ٹکٹ ہے۔", "Yeh mera tasdeeq shuda wapasi ka hawai ticket hai."),
        ("Welcome, your passport has been stamped successfully.", "خوش آمدید، آپ کے پاسپورٹ پر مہر لگا دی گئی ہے۔", "Khush aamdeed, aap ke passport par mohar laga di gayi hai."),
        ("Where is the lost and found luggage desk?", "گمشدہ سامان کا شعبہ کہاں واقع ہے؟", "Gumshuda saman ka sho'ba kahan waqia hai?"),
        ("Please describe the color and brand of your suitcase.", "براہ کرم اپنے سوٹ کیس کا رنگ اور برانڈ بتائیں۔", "Barah-e-karam apne suitcase ka rang aur brand batayein."),
        ("It is a dark blue hard-shell trolley suitcase.", "یہ گہرے نیلے رنگ کا ہارڈ شیل ٹرالی سوٹ کیس ہے۔", "Yeh gehray neelay rang ka hard-shell trolley suitcase hai."),
        ("We will trace your bag and deliver it to your hotel.", "ہم آپ کا بیگ تلاش کر کے آپ کے ہوٹل پہنچا دیں گے۔", "Hum aap ka bag talash kar ke aap ke hotel pohancha dein ge."),
        ("Can I take duty-free chocolates in my carry-on bag?", "کیا میں ڈیوٹی فری چاکلیٹس دستی بیگ میں لے جا سکتا ہوں؟", "Kya mein duty-free chocolates dasti bag mein le ja sakta hoon?"),
        ("Yes, duty-free purchases in sealed bags are permitted.", "جی ہاں، مہر بند تھیلوں میں ڈیوٹی فری سامان کی اجازت ہے۔", "Jee haan, mohar band thelon mein duty-free saman ki ijazat hai."),
        ("Where can I purchase a local tourist SIM card?", "مجھے مقامی سیاحتی سم کارڈ کہاں سے مل سکتا ہے؟", "Mujhe maqami sayahti SIM card kahan se mil sakta hai?"),
        ("Telecom booths are right past the customs exit.", "کسٹمز سے باہر نکلتے ہی موبائل نیٹ ورک کے کاؤنٹرز ہیں۔", "Customs se bahar nikaltay hi mobile network ke counters hain."),
        ("Is there an airport shuttle bus to downtown?", "کیا شہر کے مرکز کے لیے ایئرپورٹ شٹل بس چلتی ہے؟", "Kya shehar ke markaz ke liye airport shuttle bus chalti hai?"),
        ("Yes, the shuttle departs every twenty minutes.", "جی ہاں، شٹل بس ہر بیس منٹ بعد روانہ ہوتی ہے۔", "Jee haan, shuttle bus har bees minute baad rawana hoti hai."),
        ("Please keep your passport and valuables in hand luggage.", "اپنا پاسپورٹ اور قیمتی اشیاء ہمیشہ دستی بیگ میں رکھیں۔", "Apna passport aur qeemti ashya hamesha dasti bag mein rakhein."),
        ("The local time in London is 4 PM.", "لندن میں اس وقت مقامی وقت شام کے چار بجے ہے۔", "London mein is waqt maqami waqt shaam ke chaar bajay hai."),
        ("We are beginning our descent toward the airport runway.", "ہم اب رن وے کی طرف اترنے کا آغاز کر رہے ہیں۔", "Hum ab runway ki taraf utarnay ka aaghaz kar rahe hain."),
        ("Thank you for flying with our international airline.", "ہماری ایئرلائن کے ساتھ سفر کرنے کا بہت شکریہ۔", "Hamari airline ke sath safar karne ka bohat shukriya."),
        ("We wish you a memorable and fantastic holiday trip!", "ہم آپ کے لیے ایک یادگار اور شاندار چھٹیوں کی دعا کرتے ہیں!", "Hum aap ke liye aik yaadgar aur shandar chuttiyon ki dua kartay hain!")
    ]
    assert len(t7) == 50
    topics.append({
        "id": 7, "name": "Travel & Airport", "urduName": "سفر اور ہوائی اڈا", "icon": "flight",
        "desc": "Boarding flights, check-in, customs, and luggage.", "sentences": t7
    })

    # 8. Hotel & Accommodation
    t8 = [
        ("I have a room reservation under the name of Ahmad.", "میرے نام احمد کے تحت ایک کمرہ بک ہے۔", "Mere naam Ahmad ke tehat aik kamra book hai."),
        ("What time is checkout tomorrow morning?", "کل صبح چیک آؤٹ کا کیا وقت ہے؟", "Kal subah checkout ka kya waqt hai?"),
        ("Is complimentary breakfast included in the room tariff?", "کیا کمرے کے کرایے میں مفت ناشتہ شامل ہے؟", "Kya kamray ke kiraye mein muft nashta shamil hai?"),
        ("Could you send clean towels and pillows to room 405?", "کیا آپ کمرہ 405 میں صاف تولیے اور تکیے بھیج سکتے ہیں؟", "Kya aap kamra 405 mein saaf tauliye aur takiye bhej saktay hain?"),
        ("The air conditioner in my room is not cooling properly.", "میرے کمرے کا اے سی صحیح ٹھنڈک نہیں کر رہا ہے۔", "Mere kamray ka AC sahi thandak nahi kar raha hai."),
        ("Could you arrange a wake-up call at 6 AM?", "کیا آپ صبح 6 بجے کی ویک اپ کال کا انتظام کر سکتے ہیں؟", "Kya aap subah 6 bajay ki wake-up call ka intezam kar saktay hain?"),
        ("Is there high-speed Wi-Fi available in the room?", "کیا کمرے میں تیز رفتار وائی فائی دستیاب ہے؟", "Kya kamray mein taiz raftaar Wi-Fi dastiyab hai?"),
        ("Can the bellboy help us with our heavy bags?", "کیا بیلبوائے ہمارے وزنی سامان میں مدد کر سکتا ہے؟", "Kya bellboy hamare wazni saman mein madad kar sakta hai?"),
        ("I would like to extend my stay for two additional nights.", "میں اپنا قیام مزید دو راتوں کے لیے بڑھانا چاہتا ہوں۔", "Mein apna qiyam mazeed do raaton ke liye barhana chahta hoon."),
        ("Here is the room key card, thank you for the wonderful service.", "یہ لیں کمرے کا کی-کارڈ، شاندار خدمت کا بہت شکریہ۔", "Yeh lein kamray ka key-card, shandar khidmat ka bohat shukriya."),
        ("Do you have a non-smoking room on a higher floor?", "کیا اوپر والی منزل پر سگریٹ نوشی سے پاک کمرہ ہے؟", "Kya ooper wali manzil par cigarette noshi se paak kamra hai?"),
        ("Can I request a room with a scenic city view?", "کیا مجھے شہر کے خوبصورت منظر والا کمرہ مل سکتا ہے؟", "Kya mujhe shehar ke khubsurat manzar wala kamra mil sakta hai?"),
        ("Is there an electric kettle and tea bags in the room?", "کیا کمرے میں الیکٹرک کیتلی اور چائے کے ساشے موجود ہیں؟", "Kya kamray mein electric kaitli aur chaye ke sachets maujood hain?"),
        ("Could we order room service for dinner tonight?", "کیا ہم آج رات کمرے میں کھانے کا آرڈر دے سکتے ہیں؟", "Kya hum aaj raat kamray mein khanay ka order de saktay hain?"),
        ("What time is the hotel swimming pool open?", "ہوٹل کا سوئمنگ پول کس وقت کھلا ہوتا ہے؟", "Hotel ka swimming pool kis waqt khula hota hai?"),
        ("Is there a fitness gym and sauna inside the hotel?", "کیا ہوٹل کے اندر جم اور سونا کی سہولت ہے؟", "Kya hotel ke andar gym aur sauna ki sahulat hai?"),
        ("Can you store our luggage after checkout until evening?", "کیا آپ چیک آؤٹ کے بعد شام تک ہمارا سامان رکھ سکتے ہیں؟", "Kya aap checkout ke baad shaam tak hamara saman rakh saktay hain?"),
        ("Yes, our luggage storage room is completely complimentary.", "جی ہاں، سامان رکھنے کا کمرہ بالکل مفت ہے۔", "Jee haan, saman rakhnay ka kamra bilkul muft hai."),
        ("Could you arrange an airport drop-off cab for tomorrow?", "کیا آپ کل کے لیے ایئرپورٹ جانے والی ٹیکسی کا انتظام کر سکتے ہیں؟", "Kya aap kal ke liye airport jaane wali taxi ka intezam kar saktay hain?"),
        ("The hot water pressure in the shower is excellent.", "شاور میں گرم پانی کا پریشر بہت بہترین ہے۔", "Shower mein garam paani ka pressure bohat behtareen hai."),
        ("Please send someone to repair the television remote.", "ٹی وی کا ریموٹ ٹھیک کرنے کے لیے کسی کو بھیج دیں۔", "TV ka remote theek karne ke liye kisi ko bhej dein."),
        ("Do not disturb sign is hanging on the door handle.", "دروازے کے ہینڈل پر ڈو ناٹ ڈسٹرب کا بورڈ لگا ہے۔", "Darwazay ke handle par Do Not Disturb ka board laga hai."),
        ("Could housekeeping clean the room around noon?", "کیا صفائی کا عملہ دوپہر کے وقت کمرہ صاف کر سکتا ہے؟", "Kya safaai ka amla dopahar ke waqt kamra saaf kar sakta hai?"),
        ("Is there an in-room electronic safe for passports?", "کیا پاسپورٹ رکھنے کے لیے کمرے میں محفوظ تجوری ہے؟", "Kya passport rakhnay ke liye kamray mein mahfooz tijori hai?"),
        ("How do I dial the front reception desk from this phone?", "اس فون سے سامنے کے ریسپشن ڈیسک کا نمبر کیسے ملائیں؟", "Is phone se samnay ke reception desk ka number kaisay milayein?"),
        ("You simply press zero to connect to the operator.", "آپ آپریٹر سے بات کرنے کے لیے صرف صفر (0) دبائیں۔", "Aap operator se baat karne ke liye sirf sifar (0) dabayein."),
        ("We would like an extra bed or baby crib in the room.", "ہمیں کمرے میں اضافی بیڈ یا بچے کا جھولا چاہیے۔", "Humein kamray mein izafi bed ya bachay ka jhoola chahiye."),
        ("The mattress is very comfortable and soft.", "گدا بہت آرام دہ اور نرم ہے۔", "Gadda bohat aaram deh aur narm hai."),
        ("Is parking available for hotel guests on the premises?", "کیا ہوٹل میں مہمانوں کے لیے پارکنگ کی جگہ ہے؟", "Kya hotel mein mehmaanon ke liye parking ki jagah hai?"),
        ("Yes, secure underground valet parking is free.", "جی ہاں، زیر زمین محفوظ کار پارکنگ بالکل مفت ہے۔", "Jee haan, zair-e-zameen mahfooz car parking bilkul muft hai."),
        ("Can we have an early check-in at 10 AM?", "کیا ہم صبح 10 بجے جلدی چیک ان کر سکتے ہیں؟", "Kya hum subah 10 bajay jaldi check-in kar saktay hain?"),
        ("We will check room availability right now for you.", "ہم آپ کے لیے کمرے کی دستیابی ابھی چیک کرتے ہیں۔", "Hum aap ke liye kamray ki dastiyabi abhi check kartay hain."),
        ("Is there an iron and ironing board in the wardrobe?", "کیا الماری میں استری اور استری کا اسٹینڈ ہے؟", "Kya almari mein istri aur istri ka stand hai?"),
        ("Could you please bring an extra blanket to our room?", "کیا آپ ہمارے کمرے میں اضافی کمبل لا سکتے ہیں؟", "Kya aap hamare kamray mein izafi kambal laa saktay hain?"),
        ("Breakfast buffet is served on the ground floor restaurant.", "ناشتے کا بوفے گراؤنڈ فلور والے ریستوراں میں لگایا جاتا ہے۔", "Nashtay ka buffet ground floor walay restaurant mein lagaya jata hai."),
        ("What time does the breakfast buffet finish in the morning?", "صبح کا ناشتہ کس وقت ختم ہوتا ہے؟", "Subah ka nashta kis waqt khatam hota hai?"),
        ("Breakfast is served from 7 AM to 10:30 AM.", "ناشتہ صبح 7 بجے سے ساڑھے 10 بجے تک پیش کیا جاتا ہے۔", "Nashta subah 7 bajay se saarrhay 10 bajay tak pesh kiya jata hai."),
        ("Could you provide two bottles of complimentary mineral water?", "کیا آپ پینے کے پانی کی دو مفت بوتلیں فراہم کر سکتے ہیں؟", "Kya aap peenay ke paani ki do muft botalain faraham kar saktay hain?"),
        ("I need to wash and dry-clean my formal suit.", "مجھے اپنا سوٹ دھونے اور ڈرائی کلین کروانے کی ضرورت ہے۔", "Mujhe apna suit dhonay aur dry clean karwanay ki zaroorat hai."),
        ("Here is the hotel laundry bag and price list.", "یہ ہوٹل کا لانڈری بیگ اور ریٹ لسٹ ہے۔", "Yeh hotel ka laundry bag aur rate list hai."),
        ("Please make sure my laundry is ready by 5 PM.", "یقینی بنائیں کہ میری لانڈری شام 5 بجے تک تیار ہو۔", "Yaqeeni banayein ke meri laundry shaam 5 bajay tak tayar ho."),
        ("Can we get late checkout permitted until 2 PM?", "کیا ہمیں دوپہر 2 بجے تک دیر سے چیک آؤٹ کی اجازت مل سکتی ہے؟", "Kya humein dopahar 2 bajay tak der se checkout ki ijazat mil sakti hai?"),
        ("We are happy to grant you complimentary late checkout.", "ہمیں آپ کو مفت دیر سے چیک آؤٹ کی سہولت دیتے ہوئے خوشی ہے۔", "Humein aap ko muft der se checkout ki sahulat detay hue khushi hai."),
        ("The bathroom is sparkling clean and hygienic.", "باتھ روم چمکدار صاف ستھرا اور جراثیم سے پاک ہے۔", "Bathroom chamakdaar saaf suthra aur jaraseem se paak hai."),
        ("Do you offer foreign currency exchange at the desk?", "کیا آپ کاؤنٹر پر غیر ملکی کرنسی تبدیل کرتے ہیں؟", "Kya aap counter par ghair mulki currency tabdeel kartay hain?"),
        ("We enjoyed our family stay at your hotel thoroughly.", "ہم نے آپ کے ہوٹل میں اپنے قیام سے بھرپور لطف اٹھایا۔", "Hum ne aap ke hotel mein apne qiyam se bharpoor lutf uthaya."),
        ("Could you call a yellow taxi to the main lobby entrance?", "کیا آپ مین گیٹ پر ٹیکسی بلوا سکتے ہیں؟", "Kya aap main gate par taxi bulwa saktay hain?"),
        ("Please email the final invoice receipt to my address.", "براہ کرم فائنل انوائس کی رسید میری ای میل پر بھیج دیں۔", "Barah-e-karam final invoice ki raseed meri email par bhej dein."),
        ("The staff was exceedingly courteous, polite, and helpful.", "تمام عملہ انتہائی شائستہ، خوش اخلاق اور مددگار تھا۔", "Tamam amla intehai shaista, khush-ikhlaaq aur madadgaar tha."),
        ("We will definitely stay here again on our next trip!", "ہم اپنے اگلے سفر میں بھی ضرور یہیں قیام کریں گے!", "Hum apne aglay safar mein bhi zaroor yahin qiyam karein ge!")
    ]
    assert len(t8) == 50
    topics.append({
        "id": 8, "name": "Hotel & Accommodation", "urduName": "ہوٹل اور قیام", "icon": "home",
        "desc": "Booking rooms, room service, and check-out.", "sentences": t8
    })

    # 9. Directions & City Navigation (50 sentences)
    # 10. Health & Doctor Clinic (50 sentences)
    import part1_topics_9_and_10
    part1_topics_9_and_10.add_9_and_10(topics)

print("Part 1 7-10 helper loaded.")
