# -*- coding: utf-8 -*-

def add_24_to_30(topics):
    # 24. Sports & Fitness
    t24 = [
        ("Playing sports regularly keeps both body and mind sharp.", "باقاعدگی سے کھیل کھیلنا جسم اور دماغ دونوں کو چست رکھتا ہے۔", "Baqaidgi se khel khelna jism aur dimagh dono ko chust rakhta hai."),
        ("Who is your favorite international cricket batsman?", "آپ کا پسندیدہ بین الاقوامی کرکٹ بلے باز کون ہے؟", "Aap ka pasandeeda bain-ul-aqwami cricket ballebaz kaun hai?"),
        ("I go to the gymnasium for strength training four days a week.", "میں ہفتے میں چار دن ورزش کے لیے جم جاتا ہوں۔", "Mein haftay mein chaar din warzish ke liye gym jaata hoon."),
        ("Drinking enough water during intense workouts is essential.", "سخت ورزش کے دوران مناسب پانی پینا انتہائی ضروری ہے۔", "Sakht warzish ke dauran munasib paani peena intehai zaroori hai."),
        ("Pakistan won the thrilling cricket match in the final over.", "پاکستان نے آخری اوور میں سنسنی خیز کرکٹ میچ جیت لیا۔", "Pakistan ne aakhri over mein sansani khez cricket match jeet liya."),
        ("Jogging in the morning fresh air boosts your stamina.", "صبح کی تازہ ہوا میں جاگنگ کرنا جسمانی توانائی بڑھاتا ہے۔", "Subah ki taaza hawa mein jogging karna jismani tawanaai barhata hai."),
        ("Make sure you stretch your muscles thoroughly before running.", "دوڑنے سے پہلے اپنے پٹھوں کو اچھی طرح کھینچ کر گرم کریں۔", "Dorrnay se pehle apne patthon ko achi tarah kheench kar garam karein."),
        ("He scored an incredible goal from outside the penalty box.", "اس نے پنالٹی باکس کے باہر سے شاندار گول کیا۔", "Us ne penalty box ke bahar se shandar goal kiya."),
        ("Swimming is one of the best full-body cardiovascular workouts.", "تیراکی پورے جسم کی ورزش کے لیے بہترین ثابت ہوتی ہے۔", "Tairaki pooray jism ki warzish ke liye behtareen sabit hoti hai."),
        ("Consistency and balanced nutrition are the keys to true fitness.", "مسلسل کوشش اور متوازن غذا ہی اصل فٹنس کا راز ہیں۔", "Musalsal koshish aur mutawazin ghiza hi asal fitness ka raaz hain."),
        ("What is your current bench press and squat personal record?", "آپ کا بینچ پریس اور اسکواٹ کا ذاتی ریکارڈ کیا ہے؟", "Aap ka bench press aur squat ka zaati record kya hai?"),
        ("I aim to complete ten thousand steps every single day.", "میرا مقصد روزانہ دس ہزار قدم چلنے کا ہدف پورا کرنا ہے۔", "Mera maqsad rozana das hazar qadam chalnay ka hadaf poora karna hai."),
        ("Table tennis requires lightning-fast reflexes and focus.", "ٹیبل ٹینس کے لیے بجلی جیسی تیز رفتاری اور توجہ درکار ہے۔", "Table tennis ke liye bijli jaisi taiz-raftari aur tawajjo darkaar hai."),
        ("Our local football club reached the tournament finals.", "ہمارا مقامی فٹ بال کلب ٹورنامنٹ کے فائنل میں پہنچ گیا۔", "Hamara maqami football club tournament ke final mein pohanch gaya."),
        ("Proper breathing technique prevents workout fatigue.", "سانس لینے کا درست طریقہ ورزش میں جلد تھکنے سے بچاتا ہے۔", "Saans lenay ka durust tareeqa warzish mein jald thaknay se bachata hai."),
        ("I am training hard to run a half marathon next winter.", "میں اگلی سردیوں میں ہاف میراتھن دوڑنے کی سخت تیاری کر رہا ہوں۔", "Mein agli sardiyon mein half marathon dorrnay ki sakht tayari kar raha hoon."),
        ("Badminton is a fantastic sport for agility and endurance.", "بیڈمنٹن چستی اور قوت مدافعت کے لیے ایک زبردست کھیل ہے۔", "Badminton chusti aur quwwat-e-mudafe'at ke liye aik zabardast khel hai."),
        ("Eat protein-rich wholesome food to repair tired muscles.", "تھکے ہوئے پٹھوں کی بحالی کے لیے پروٹین سے بھرپور غذا لیں۔", "Thakay hue patthon ki bahali ke liye protein se bharpoor ghiza lein."),
        ("Warm-up exercises protect your joints from sudden injuries.", "وارم اپ ورزشیں آپ کے جوڑوں کو اچانک چوٹوں سے محفوظ رکھتی ہیں۔", "Warm-up warzishein aap ke jorron ko achanak choton se mahfooz rakhti hain."),
        ("The cricket umpire raised his finger for an LBW decision.", "امپائر نے ایل بی ڈبلیو پر انگلی اٹھا کر آؤٹ قرار دیا۔", "Umpire ne LBW par ungli utha kar out qarar diya."),
        ("Cycling improves cardiovascular health and leg strength.", "سائیکل چلانا دل کی صحت اور ٹانگوں کی طاقت کو بہتر بناتا ہے۔", "Cycle chalana dil ki sehat aur taangon ki taaqat ko behtar banata hai."),
        ("He practices martial arts and holds a black belt in Karate.", "وہ مارشل آرٹس کی مشق کرتا ہے اور کراٹے میں بلیک بیلٹ ہے۔", "Woh martial arts ki mashq karta hai aur karate mein black belt hai."),
        ("Yoga enhances body flexibility, balance, and inner calm.", "یوگا جسم کی لچک، توازن اور اندرونی سکون کو بڑھاتا ہے۔", "Yoga jism ki lachak, tawazun aur androoni sukoon ko barhata hai."),
        ("Always wear supportive athletic running shoes for jogging.", "جاگنگ کے لیے ہمیشہ معیاری اسپورٹس جوتے استعمال کریں۔", "Jogging ke liye hamesha meyaari sports jootay istemal karein."),
        ("The stadium was packed with fifty thousand cheering fans.", "اسٹیڈیم پچاس ہزار پرجوش شائقین سے کھچا کھچ بھرا ہوا تھا۔", "Stadium 50 hazar purjosh shayiqeen se khacha-khach bhara hua tha."),
        ("Discipline in daily training beats raw talent every time.", "روزانہ کی باقاعدہ محنت بغیر محنت کے ہنر کو مات دے دیتی ہے۔", "Rozana ki baqaida mehnat baghair mehnat ke hunar ko maat de deti hai."),
        ("Drink an electrolyte drink on hot summer match days.", "گرمی کے دنوں میں میچ کے دوران نمکیات والا مشروب پئیں۔", "Garmi ke dinon mein match ke dauran namkiyaat wala mashroob piyein."),
        ("Taking rest days is vital for muscle growth and recovery.", "پٹھوں کی مضبوطی اور بحالی کے لیے آرام کے دن ضروری ہیں۔", "Patthon ki mazbooti aur bahali ke liye aaram ke din zaroori hain."),
        ("He bowled a fierce yorker that shattered the stumps.", "اس نے تیز یارکر پھینکی جس نے وکٹیں اکھاڑ دیں۔", "Us ne taiz yorker phainki jis ne wicketein ukhaar dein."),
        ("Fitness is not a temporary trend; it is a lifelong lifestyle.", "فٹنس کوئی عارضی شوق نہیں بلکہ زندگی بھر کا طرزِ حیات ہے۔", "Fitness koi aarzi shauq nahi balkay zindagi bhar ka tarz-e-hayat hai."),
        ("How many pushups and pullups can you do consecutively?", "آپ مسلسل کتنے پش اپس اور پل اپس لگا سکتے ہیں؟", "Aap musalsal kitnay pushups aur pullups laga saktay hain?"),
        ("A thirty-minute brisk walk daily reduces heart risks.", "روزانہ تیس منٹ تیز قدموں کی واک دل کی بیماریوں سے بچاتی ہے۔", "Rozana 30 minute taiz qadmon ki walk dil ki bimariyon se bachati hai."),
        ("Squats build powerful legs and improve core stability.", "اسکواٹس ٹانگوں کو طاقتور اور جسمانی توازن کو بہتر بناتے ہیں۔", "Squats taangon ko taqatwar aur jismani tawazun ko behtar banatay hain."),
        ("Our school cricket team won the inter-city championship cup.", "ہماری اسکول کرکٹ ٹیم نے انٹر سٹی چیمپئن شپ ٹرافی جیت لی۔", "Hamari school cricket team ne inter-city championship trophy jeet li."),
        ("Avoid overtraining to prevent burnout and muscle strains.", "تھکن اور پٹھوں کے کھنچاؤ سے بچنے کے لیے حد سے زیادہ ورزش نہ کریں۔", "Thakan aur patthon ke khinchao se bachnay ke liye hadd se zyada warzish na karein."),
        ("He made a breathtaking catch diving near the boundary line.", "اس نے باؤنڈری لائن کے قریب ہوا میں غوطہ لگا کر شاندار کیچ پکڑا۔", "Us ne boundary line ke qareeb hawa mein ghota laga kar shandar catch pakrra."),
        ("Eat wholesome fruits and almonds before your workout session.", "ورزش شروع کرنے سے پہلے تازہ پھل اور بادام کھائیں۔", "Warzish shuru karne se pehle taaza phal aur badaam khayein."),
        ("Maintain a straight spine when lifting heavy weights.", "وزن اٹھاتے وقت اپنی کمر کو ہمیشہ بالکل سیدھا رکھیں۔", "Wazan uthaatay waqt apni kamar ko hamesha bilkul seedha rakhein."),
        ("The referee blew the whistle to signal the end of the game.", "ریفری نے میچ کے اختتام کی سیٹی بجا دی۔", "Referee ne match ke ikhtitaam ki seeti baja di."),
        ("Sportsmanship teaches us how to win with grace and lose with dignity.", "کھلاڑی کا جذبہ جیت میں عاجزی اور ہار میں وقار سکھاتا ہے۔", "Khilari ka jazba jeet mein aajzi aur haar mein waqaar sikhata hai."),
        ("I love hiking up mountain trails on cool autumn mornings.", "خوشگوار صبح میں پہاڑی راستوں پر چڑھنا میرا پسندیدہ کھیل ہے۔", "Khushgawar subah mein pahari raston par charrhna mera pasandeeda khel hai."),
        ("Staying active keeps your energy levels high all day long.", "متحرک رہنا سارا دن جسم میں چستی اور توانائی برقرار رکھتا ہے۔", "Mutaharrik rehna sara din jism mein chusti aur tawanaai barqarar rakhta hai."),
        ("He won the gold medal in the hundred-meter sprint.", "اس نے سو میٹر کی دوڑ میں گولڈ میڈل جیتا۔", "Us ne 100 meter ki dorr mein gold medal jeeta."),
        ("Team chemistry is essential to win tough tournaments.", "مشکل مقابلے جیتنے کے لیے ٹیم کا باہمی تال میل لازمی ہے۔", "Mushkil muqablay jeetnay ke liye team ka baahmi taal-mail laazmi hai."),
        ("A healthy outside starts from healthy nutrition from the inside.", "خوبصورت اور مضبوط جسم اندرونی اچھی خوراک سے بنتا ہے۔", "Khubsurat aur mazboot jism androoni achi khorak se banta hai."),
        ("Track your physical progress weekly instead of daily.", "روزانہ کے بجائے ہر ہفتے اپنی جسمانی ترقی کا معائنہ کریں۔", "Rozana ke bajaye har haftay apni jismani taraqi ka muaina karein."),
        ("He is a phenomenal captain who leads his team by example.", "وہ ایک زبردست کپتان ہے جو خود مثال بن کر ٹیم کی رہنمائی کرتا ہے۔", "Woh aik zabardast kaptaan hai jo khud misaal ban kar team ki rehnumai karta hai."),
        ("Never underestimate the power of consistent daily effort.", "روزانہ کی مستقل محنت کی طاقت کو کبھی کم مت سمجھیں۔", "Rozana ki mustaqil mehnat ki taaqat ko kabhi kam mat samjhein."),
        ("Physical health and spiritual peace go hand in hand.", "جسمانی صحت اور روحانی سکون ایک دوسرے کے ساتھ جڑے ہیں۔", "Jismani sehat aur roohani sukoon aik doosray ke sath jurray hain."),
        ("Keep training with heart, dedication, and unwavering focus!", "لگن، محنت اور غیر متزلزل توجہ کے ساتھ اپنی مشق جاری رکھیں!", "Lagan, mehnat aur ghair mutazalzil tawajjo ke sath apni mashq jaari rakhein!")
    ]
    topics.append({
        "id": 24, "name": "Sports & Fitness", "urduName": "کھیل اور ورزش", "icon": "sports_cricket",
        "desc": "Cricket, football, gym workouts, jogging, and health.", "sentences": t24
    })

    # Add topics 25 to 30 via part3_topics_25_to_30
    import part3_topics_25_to_30
    part3_topics_25_to_30.add_25_to_30(topics)

print("Part 3 24-30 loaded.")
