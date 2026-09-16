# -*- coding: utf-8 -*-

def add_6_to_10(topics):
    # 6. Job Interview & Career
    t6 = [
        ("Please tell me a little about yourself.", "برائے مہربانی اپنے بارے میں کچھ بتائیے۔", "Baraye meharbani apne baare mein kuch batayiye."),
        ("What are your greatest professional strengths?", "آپ کی سب سے بڑی پیشہ ورانہ خوبیاں کیا ہیں؟", "Aap ki sab se barri peshawarana khoobiyan kya hain?"),
        ("Why do you want to join our company?", "آپ ہماری کمپنی میں شمولیت کیوں اختیار کرنا چاہتے ہیں؟", "Aap hamari company mein shamooliyat kyun ikhtiyar karna chahtay hain?"),
        ("I have five years of experience in software development.", "میرے پاس سافٹ ویئر ڈویلپمنٹ کا پانچ سالہ تجربہ ہے۔", "Mere paas software development ka paanch saala tajruba hai."),
        ("How do you handle pressure and tight deadlines?", "آپ دباؤ اور سخت ڈیڈ لائنز کو کیسے سنبھالتے ہیں؟", "Aap dabao aur sakht deadlines ko kaisay sambhaaltay hain?"),
        ("I am a quick learner and a dedicated team player.", "میں جلدی سیکھنے والا اور لگن سے کام کرنے والا ٹیم ممبر ہوں۔", "Mein jaldi seekhnay wala aur lagan se kaam karne wala team member hoon."),
        ("Where do you see yourself in five years?", "آپ پانچ سال بعد خود کو کہاں دیکھتے ہیں؟", "Aap paanch saal baad khud ko kahan dekhtay hain?"),
        ("What are your salary expectations for this role?", "اس عہدے کے لیے آپ کی تنخواہ کی کیا توقعات ہیں؟", "Is ohday ke liye aap ki tankhwah ki kya tawwaqaat hain?"),
        ("Do you have any questions for the interview panel?", "کیا آپ کے پاس انٹرویو پینل کے لیے کوئی سوال ہے؟", "Kya aap ke paas interview panel ke liye koi sawal hai?"),
        ("We will notify you about the final decision via email.", "ہم حتمی فیصلے کے بارے میں آپ کو ای میل پر مطلع کریں گے۔", "Hum hatmi faislay ke baare mein aap ko email par muttala karein ge."),
        ("What was your proudest career achievement so far?", "آپ کی اب تک کی سب سے بڑی پیشہ ورانہ کامیابی کیا ہے؟", "Aap ki ab tak ki sab se barri peshawarana kamyabi kya hai?"),
        ("How do you resolve conflicts within your team?", "آپ ٹیم میں پیدا ہونے والے تنازعات کو کیسے حل کرتے ہیں؟", "Aap team mein paida honay walay tanaza'at ko kaisay hal kartay hain?"),
        ("Why did you decide to leave your previous job?", "آپ نے اپنی پچھلی نوکری چھوڑنے کا فیصلہ کیوں کیا؟", "Aap ne apni pichli naukri chhorrnay ka faisla kyun kiya?"),
        ("I am seeking new growth opportunities and challenges.", "میں ترقی کے نئے مواقع اور چیلنجز تلاش کر رہا ہوں۔", "Mein taraqi ke naye mauqay aur challenges talash kar raha hoon."),
        ("Can you work comfortably in rotating shifts?", "کیا آپ شفٹوں میں آسانی سے کام کر سکتے ہیں؟", "Kya aap shifton mein aasani se kaam kar saktay hain?"),
        ("I am fully open to flexible work arrangements.", "میں کام کے لچکدار نظام کے لیے بالکل تیار ہوں۔", "Mein kaam ke lachakdar nizaam ke liye bilkul tayar hoon."),
        ("What tools and software are you proficient in?", "آپ کن سافٹ وئیرز اور ٹولز کے استعمال میں ماہر ہیں؟", "Aap kin softwares aur tools ke istemal mein mahir hain?"),
        ("I have advanced skills in Python, Java, and SQL.", "میرے پاس پائتھن، جاوا اور ایس کیو ایل میں بہترین مہارت ہے۔", "Mere paas Python, Java aur SQL mein behtareen maharat hai."),
        ("Tell me about a difficult challenge you overcame.", "کسی ایسے مشکل چیلنج کے بارے میں بتائیں جس پر آپ نے قابو پایا۔", "Kisi aisay mushkil challenge ke baare mein batayein jis par aap ne qaaboo paaya."),
        ("I stayed calm, analyzed the root problem, and solved it.", "میں نے پرسکون رہ کر مسئلے کی جڑ سمجھی اور اسے حل کیا۔", "Mein ne pursukoon reh kar maslay ki jarr samjhi aur usey hal kiya."),
        ("How do you stay updated with industry trends?", "آپ نئی معلومات اور رجحانات سے کیسے باخبر رہتے ہیں؟", "Aap nayi maloomat aur rujhanat se kaisay ba-khabar rehtay hain?"),
        ("I read tech journals and take online courses regularly.", "میں باقاعدگی سے مضامین پڑھتا ہوں اور کورسز کرتا ہوں۔", "Mein baqaidgi se mazameen parhta hoon aur courses karta hoon."),
        ("Are you willing to relocate to another city?", "کیا آپ دوسرے شہر منتقل ہونے کے لیے تیار ہیں؟", "Kya aap doosray shehar muntaqil honay ke liye tayar hain?"),
        ("Yes, I am happy to relocate for the right role.", "جی ہاں، اچھے موقع کے لیے میں منتقل ہونے کو تیار ہوں۔", "Jee haan, achay mauqay ke liye mein muntaqil honay ko tayar hoon."),
        ("What makes you the ideal candidate for this post?", "کون سی بات آپ کو اس پوسٹ کے لیے سب سے موزوں بناتی ہے؟", "Kaun si baat aap ko is post ke liye sab se mauzoon banati hai?"),
        ("My technical expertise and strong work ethic match your needs.", "میری فنی مہارت اور محنت کا جذبہ آپ کی ضروریات سے مطابقت رکھتا ہے۔", "Meri fanni maharat aur mehnat ka jazba aap ki zarooriyat se mutabiqat rakhta hai."),
        ("How do you prioritize multiple urgent assignments?", "آپ بیک وقت کئی ضروری کاموں میں ترجیحات کیسے طے کرتے ہیں؟", "Aap bayk waqt kayi zaroori kaamon mein tarjeehat kaisay tay kartay hain?"),
        ("I organize tasks by impact and urgency in a checklist.", "میں کاموں کو ان کی اہمیت اور وقت کے لحاظ سے ترتیب دیتا ہوں۔", "Mein kaamon ko un ki ahmiyat aur waqt ke lehaaz se tarteeb deta hoon."),
        ("Have you ever managed a team of professionals?", "کیا آپ نے کبھی کسی ٹیم کی سربراہی کی ہے؟", "Kya aap ne kabhi kisi team ki sarbarahi ki hai?"),
        ("Yes, I led a cross-functional team of eight members.", "جی ہاں، میں نے آٹھ افراد کی ٹیم کی قیادت کی ہے۔", "Jee haan, mein ne aath afraad ki team ki qayadat ki hai."),
        ("What motivates you to perform at your best?", "کون سی چیز آپ کو بہترین کارکردگی دکھانے پر ابھارتی ہے؟", "Kaun si cheez aap ko behtareen karkardagi dikhanay par ubhaarti hai?"),
        ("Solving complex problems and creating user value motivates me.", "مشکل مسائل حل کرنا اور صارفین کو فائدہ پہنچانا مجھے متحرک کرتا ہے۔", "Mushkil masail hal karna aur sarifeen ko faida pohanchana mujhe mutaharrik karta hai."),
        ("Can you provide references from previous managers?", "کیا آپ سابقہ منیجرز کے تصدیقی حوالے فراہم کر سکتے ہیں؟", "Kya aap sabqa managers ke tasdeeqi hawalay faraham kar saktay hain?"),
        ("Yes, I have letters of recommendation ready.", "جی ہاں، میرے پاس سفارشی خطوط موجود ہیں۔", "Jee haan, mere paas sifarishi khutooth maujood hain."),
        ("What is your notice period at your current job?", "آپ کی موجودہ نوکری کا نوٹس پیریڈ کتنا ہے؟", "Aap ki maujooda naukri ka notice period kitna hai?"),
        ("I can join your organization within two weeks.", "میں دو ہفتوں کے اندر آپ کا ادارہ جوائن کر سکتا ہوں۔", "Mein do hafton ke andar aap ka idara join kar sakta hoon."),
        ("How do you accept critical feedback from seniors?", "آپ سینئرز کی طرف سے اصلاحی رائے کیسے قبول کرتے ہیں؟", "Aap seniors ki taraf se islahi raye kaisay qabool kartay hain?"),
        ("I view feedback as a valuable learning opportunity.", "میں تعمیری رائے کو سیکھنے کا بہترین موقع سمجھتا ہوں۔", "Mein tameeri raye ko seekhnay ka behtareen mauqa samajhta hoon."),
        ("Do you prefer independent work or collaborative teamwork?", "کیا آپ تنہا کام پسند کرتے ہیں یا ٹیم کے ساتھ؟", "Kya aap tanha kaam pasand kartay hain ya team ke sath?"),
        ("I thrive in both environments depending on task requirements.", "میں کام کی نوعیت کے مطابق دونوں طریقوں میں بہترین کام کرتا ہوں۔", "Mein kaam ki noiyat ke mutabiq dono tareeqon mein behtareen kaam karta hoon."),
        ("Tell me about a time you took initiative.", "کوئی ایسا واقعہ بتائیں جب آپ نے خود آگے بڑھ کر پہل کی۔", "Koi aisa waqia batayein jab aap ne khud aagay barh kar pehal ki."),
        ("I designed an automated tool that saved 10 hours weekly.", "میں نے ایک خودکار ٹول بنایا جس نے ہفتہ وار دس گھنٹے بچائے۔", "Mein ne aik khudkaar tool banaya jis ne hafta-waar das ghantay bachaye."),
        ("What are your long-term career aspirations?", "آپ کے طویل مدتی کیریئر کے کیا اہداف ہیں؟", "Aap ke taweel muddati career ke kya ahdaaf hain?"),
        ("I aspire to grow into a senior leadership role.", "میری خواہش ہے کہ میں سینئر لیڈرشپ کے منصب تک پہنچوں۔", "Meri khwahish hai ke mein senior leadership ke mansab tak pohanchon."),
        ("We are very impressed by your background and portfolio.", "ہم آپ کے تجربے اور پورٹ فولیو سے بہت متاثر ہوئے ہیں۔", "Hum aap ke tajrubay aur portfolio se bohat mutaasir hue hain."),
        ("Thank you very much for this wonderful opportunity.", "اس شاندار موقع کے لیے آپ کا بہت بہت شکریہ۔", "Is shandar mauqay ke liye aap ka bohat bohat shukriya."),
        ("When can we expect to hear back from HR?", "ہمیں ایچ آر کی طرف سے کب تک جواب کی توقع رکھنی چاہیے؟", "Humein HR ki taraf se kab tak jawab ki tawaqqo rakhni chahiye?"),
        ("Our HR team will contact you by Friday afternoon.", "ہماری ایچ آر ٹیم جمعہ کی دوپہر تک آپ سے رابطہ کرے گی۔", "Hamari HR team jumma ki dopahar tak aap se rabta karay gi."),
        ("It was a pleasure speaking with all of you.", "آپ سب سے گفتگو کر کے بہت خوشی ہوئی۔", "Aap sab se guftagu kar ke bohat khushi hui."),
        ("I look forward to contributing to your esteemed team.", "مجھے آپ کی معزز ٹیم میں حصہ ڈالنے کا انتظار رہے گا۔", "Mujhe aap ki moazzaz team mein hissa daalnay ka intezar rahay ga.")
    ]
    assert len(t6) == 50
    topics.append({
        "id": 6, "name": "Job Interview & Career", "urduName": "ملازمت کا انٹرویو", "icon": "school",
        "desc": "Common questions and impressive answers during interviews.", "sentences": t6
    })

    # Add topics 7 to 10
    import part1_topics_7_to_10
    part1_topics_7_to_10.add_7_to_10(topics)

print("Part 1 6-10 helper loaded.")
