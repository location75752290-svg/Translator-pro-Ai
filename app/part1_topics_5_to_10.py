# -*- coding: utf-8 -*-

def add_5_to_10(topics):
    # 5. Office & Career
    t5 = []
    t5.extend([
        ("Let's schedule a meeting for tomorrow at 10 AM.", "آئیں کل صبح 10 بجے کے لیے ایک میٹنگ طے کرتے ہیں۔", "Aayein kal subah 10 bajay ke liye aik meeting tay kartay hain."),
        ("I sent the project report to your email.", "میں نے پروجیکٹ کی رپورٹ آپ کی ای میل پر بھیج دی ہے۔", "Mein ne project ki report aap ki email par bhej di hai."),
        ("We must meet the client deadline by Friday.", "ہمیں جمعہ تک کلائنٹ کی ڈیڈ لائن لازمی پوری کرنی ہے۔", "Humein jumma tak client ki deadline laazmi poori karni hai."),
        ("She gave a very persuasive presentation today.", "اس نے آج بہت اثر انگیز پریزنٹیشن پیش کی۔", "Us ne aaj bohat asar-angeez presentation pesh ki."),
        ("I will discuss this proposal with the manager.", "میں اس تجویز پر منیجر سے بات چیت کروں گا۔", "Mein is tajweez par manager se baat cheet karunga."),
        ("Could you please review the attached document?", "کیا آپ برائے مہربانی منسلک دستاویز کا جائزہ لے سکتے ہیں؟", "Kya aap baraye meharbani munsalik dastaweez ka jaiza le saktay hain?"),
        ("Team collaboration is crucial for company growth.", "کمپنی کی ترقی کے لیے ٹیم کا باہمی تعاون لازمی ہے۔", "Company ki taraqi ke liye team ka baahmi ta'awun laazmi hai."),
        ("I am currently working from home on Mondays.", "میں فی الحال پیر کے دن گھر سے کام کرتا ہوں۔", "Mein filhaal peer ke din ghar se kaam karta hoon."),
        ("He received a well-deserved salary bonus.", "اسے محنت کے صلے میں حق دار بونس ملا۔", "Usey mehnat ke silay mein haqdaar bonus mila."),
        ("Please submit your leave application in advance.", "براہ کرم اپنی چھٹی کی درخواست پہلے سے جمع کروائیں۔", "Barah-e-karam apni chhutti ki darkhwast pehle se jama karwayein."),
        ("Our team achieved record quarterly sales.", "ہماری ٹیم نے اس سہ ماہی میں ریکارڈ سیلز حاصل کیں۔", "Hamari team ne is seh maahi mein record sales haasil keen."),
        ("Who is leading the new marketing campaign?", "نئی مارکیٹنگ مہم کی قیادت کون کر رہا ہے؟", "Nayi marketing muhim ki qayadat kaun kar raha hai?"),
        ("Please turn on your camera during the Zoom call.", "زوم کال کے دوران اپنا کیمرہ آن کر لیں۔", "Zoom call ke dauran apna camera on kar lein."),
        ("I will update the team spreadsheet by afternoon.", "میں دوپہر تک ٹیم کی اسپریڈشیٹ اپ ڈیٹ کر دوں گا۔", "Mein dopahar tak team ki spreadsheet update kar doonga."),
        ("We need to cut unnecessary company expenses.", "ہمیں کمپنی کے غیر ضروری اخراجات کم کرنے کی ضرورت ہے۔", "Humein company ke ghair zaroori ikhrajat kam karne ki zaroorat hai."),
        ("The CEO announced a new expansion plan today.", "سی ای او نے آج نئے توسیعی منصوبے کا اعلان کیا۔", "CEO ne aaj naye tawsee'i mansoobay ka elaan kiya."),
        ("Let's brainstorm creative ideas for the client.", "آئیں کلائنٹ کے لیے تخلیقی خیالات پر غور و فکر کرتے ہیں۔", "Aayein client ke liye takhleeqi khayalaat par ghaur-o-fikr kartay hain."),
        ("Could you print five copies of this agenda?", "کیا آپ اس ایجنڈے کی پانچ کاپیاں پرنٹ کر سکتے ہیں؟", "Kya aap is agenda ki paanch copyan print kar saktay hain?"),
        ("I have an appraisal review meeting next week.", "اگلے ہفتے میری کارکردگی کے جائزے کی میٹنگ ہے۔", "Aglay haftay meri karkardagi ke jaizay ki meeting hai."),
        ("Please keep all confidential files password protected.", "تمام خفیہ فائلوں کو پاس ورڈ کے ذریعے محفوظ رکھیں۔", "Tamam khufia files ko password ke zariye mahfooz rakhein."),
        ("We are hiring two junior graphic designers.", "ہم دو جونیئر گرافک ڈیزائنرز بھرتی کر رہے ہیں۔", "Hum do junior graphic designers bharti kar rahe hain."),
        ("He is very disciplined and punctual at work.", "وہ کام میں بہت باقاعدہ اور وقت کا پابند ہے۔", "Woh kaam mein bohat baqaida aur waqt ka paband hai."),
        ("Can we have a quick five-minute sync call?", "کیا ہم پانچ منٹ کی فوری فون کال کر سکتے ہیں؟", "Kya hum paanch minute ki fauri phone call kar saktay hain?"),
        ("I will share the meeting minutes by tonight.", "میں آج رات تک میٹنگ کے اہم نکات شیئر کروں گا۔", "Mein aaj raat tak meeting ke ahem nukat share karunga."),
        ("Our department won the employee excellence award.", "ہمارے شعبے نے ملازمین کی عمدگی کا ایوارڈ جیتا۔", "Hamare sho'bay ne mulazimeen ki umdagi ka award jeeta."),
        ("Please double-check the figures in the invoice.", "انوائس میں درج اعداد و شمار کو دوبارہ چیک کر لیں۔", "Invoice mein darj aadaad-o-shumar ko dobara check kar lein."),
        ("We should automate this repetitive manual task.", "ہمیں اس بار بار ہونے والے دستی کام کو خودکار کرنا چاہیے۔", "Humein is baar baar honay walay dasti kaam ko khudkaar karna chahiye."),
        ("Effective communication resolves office friction.", "مؤثر رابطہ دفتری تناؤ کو دور کرتا ہے۔", "Mo'assir rabta daftari tanao ko door karta hai."),
        ("The server maintenance is scheduled for midnight.", "سرور کی دیکھ بھال رات گئے طے کی گئی ہے۔", "Server ki dekh bhaal raat gaye tay ki gayi hai."),
        ("Let's delegate tasks according to each person's skills.", "ہر فرد کی مہارت کے مطابق کام تقسیم کریں۔", "Har fard ki maharat ke mutabiq kaam taqseem karein."),
        ("He was promoted to senior project manager.", "اسے سینئر پروجیکٹ منیجر کے عہدے پر ترقی دی گئی۔", "Usey senior project manager ke ohday par taraqi di gayi."),
        ("Please keep me in the loop regarding this client.", "اس کلائنٹ کے بارے میں مجھے باخبر رکھیے گا۔", "Is client ke baare mein mujhe ba-khabar rakhiye ga."),
        ("We need to prepare a detailed financial forecast.", "ہمیں تفصیلی مالیاتی تخمینہ تیار کرنے کی ضرورت ہے۔", "Humein tafseeli maalyati takhmeena tayar karne ki zaroorat hai."),
        ("Our company offers medical insurance benefits.", "ہماری کمپنی میڈیکل انشورنس کی سہولت فراہم کرتی ہے۔", "Hamari company medical insurance ki sahulat faraham karti hai."),
        ("Please clear your desk before leaving the office.", "دفتر سے نکلنے سے پہلے اپنی میز صاف کریں۔", "Daftar se nikaltay waqt apni maiz saaf karein."),
        ("We must comply with international safety standards.", "ہمیں بین الاقوامی حفاظتی معیارات کی پابندی کرنی چاہیے۔", "Humein bain-ul-aqwami hifazati meyaaraat ki pabandi karni chahiye."),
        ("I will be out of office until next Monday.", "میں اگلے پیر تک دفتر سے رخصت پر رہوں گا۔", "Mein aglay peer tak daftar se rukhsat par rahoonga."),
        ("For urgent matters, you can call my cell phone.", "ہنگامی معاملے کی صورت میں آپ میرے موبائل پر کال کر سکتے ہیں۔", "Hangami maamlay ki soorat mein aap mere mobile par call kar saktay hain."),
        ("Our team works with high integrity and honesty.", "ہماری ٹیم دیانتداری اور سچائی سے کام کرتی ہے۔", "Hamari team diyanat-dari aur sachaai se kaam karti hai."),
        ("We signed a long-term partnership contract.", "ہم نے طویل مدتی شراکت داری کے معاہدے پر دستخط کیے۔", "Hum ne taweel muddati sharakat-daari ke maahday par dastakhat kiye."),
        ("Constructive feedback helps professional growth.", "تعمیری تنقید پیشہ ورانہ ترقی میں مدد دیتی ہے۔", "Tameeri tanqeed peshawarana taraqi mein madad deti hai."),
        ("Let's prioritize critical bugs before the release.", "ریلیز سے پہلے سنگین مسائل کو حل کرنا ترجیح بنائیں۔", "Release se pehle sangeen masail ko hal karna tarjeeh banayein."),
        ("Please mute your microphone when not speaking.", "جب نہ بول رہے ہوں تو مائیک میوٹ رکھیں۔", "Jab na bol rahe hon to mic mute rakhein."),
        ("Hard work and loyalty always pay off.", "سخت محنت اور وفاداری کا پھل ہمیشہ ملتا ہے۔", "Sakht mehnat aur wafadari ka phal hamesha milta hai."),
        ("I sent the payment confirmation slip.", "میں نے ادائیگی کی تصدیقی رسید بھیج دی ہے۔", "Mein ne adayigi ki tasdeeqi raseed bhej di hai."),
        ("We have an all-hands meeting this Friday.", "اس جمعہ کو پوری کمپنی کا عمومی اجلاس ہے۔", "Is jumma ko poori company ka umoomi ijlas hai."),
        ("Stay focused and manage your work hours wisely.", "اپنے کام کے اوقات کا دانشمندی سے استعمال کریں۔", "Apne kaam ke auqaat ka danishmandi se istemal karein."),
        ("Work-life balance is essential for health.", "صحت کے لیے کام اور ذاتی زندگی میں توازن ضروری ہے۔", "Sehat ke liye kaam aur zaati zindagi mein tawazun zaroori hai."),
        ("We appreciate your dedication to the organization.", "ہم ادارے کے لیے آپ کی لگن اور محنت کی قدر کرتے ہیں۔", "Hum idaray ke liye aap ki lagan aur mehnat ki qadr kartay hain."),
        ("Let's conclude today's productive meeting.", "آئیں آج کی نتیجہ خیز میٹنگ کا اختتام کرتے ہیں۔", "Aayein aaj ki nateeja-khez meeting ka ikhtitaam kartay hain.")
    ])
    assert len(t5) == 50
    topics.append({
        "id": 5, "name": "Office & Career", "urduName": "دفتر اور ملازمت", "icon": "work",
        "desc": "Workplace conversations, meetings, and project deadlines.", "sentences": t5
    })

    # Add topics 6, 7, 8, 9, 10
    import part1_topics_6_to_10
    part1_topics_6_to_10.add_6_to_10(topics)

print("Part 1 5-10 helper loaded.")
