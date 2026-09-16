# -*- coding: utf-8 -*-
import os
from writer_helper import write_part_file

def get_part3_topics():
    topics = []
    
    # 21. Asking Questions & Clarification
    t21 = [
        ("Could you please explain what you mean by that?", "کیا آپ برائے مہربانی وضاحت کر سکتے ہیں کہ آپ کا کیا مطلب ہے؟", "Kya aap baraye meharbani wazahat kar saktay hain ke aap ka kya matlab hai?"),
        ("I didn't quite catch that, could you repeat it?", "میں صحیح سن نہیں پایا، کیا آپ دہرا سکتے ہیں؟", "Mein sahi sun nahi paya, kya aap dohra saktay hain?"),
        ("Could you speak a little slower, please?", "کیا آپ تھوڑا آہستہ بول سکتے ہیں، برائے مہربانی؟", "Kya aap thora aahista bol saktay hain, baraye meharbani?"),
        ("What does this English word mean in Urdu?", "اس انگریزی لفظ کا اردو میں کیا مطلب ہے؟", "Is angrezi lafz ka Urdu mein kya matlab hai?"),
        ("How do you pronounce this word correctly?", "آپ اس لفظ کا درست تلفظ کیسے کرتے ہیں؟", "Aap is lafz ka durust talaffuz kaisay kartay hain?"),
        ("Could you give me an example sentence using this?", "کیا آپ اس کا استعمال کرتے ہوئے ایک مثال دے سکتے ہیں؟", "Kya aap is ka istemal kartay hue aik misaal de saktay hain?"),
        ("Are you saying that we should postpone the launch?", "کیا آپ کا مطلب یہ ہے کہ ہمیں لانچ ملتوی کرنی چاہیے؟", "Kya aap ka matlab yeh hai ke humein launch multawi karni chahiye?"),
        ("Excuse me, could I ask a quick question?", "معاف کیجیے گا، کیا میں ایک فوری سوال پوچھ سکتا ہوں؟", "Maaf kijiye ga, kya mein aik fauri sawal pooch sakta hoon?"),
        ("Where can I find more detailed documentation on this?", "مجھے اس بارے میں مزید تفصیلی معلومات کہاں سے ملیں گی؟", "Mujhe is baare mein mazeed tafseeli maloomat kahan se milein gi?"),
        ("Did I understand your point correctly?", "کیا میں نے آپ کا نکتہ درست سمجھا؟", "Kya mein ne aap ka nukta durust samjha?"),
        ("Could you clarify the second step of the procedure?", "کیا آپ طریقہ کار کے دوسرے مرحلے کی وضاحت کر سکتے ہیں؟", "Kya aap tareeqa-kaar ke doosray marhalay ki wazahat kar saktay hain?"),
        ("What is the main difference between these two options?", "ان دونوں اختیارات میں بنیادی فرق کیا ہے؟", "In dono ikhtiyaraat mein bunyadi farq kya hai?"),
        ("Who should I contact if I run into any problem?", "اگر مجھے کوئی مسئلہ پیش آئے تو کس سے رابطہ کرنا چاہیے؟", "Agar mujhe koi masla pesh aaye to kis se rabta karna chahiye?"),
        ("Why is this step necessary for the process?", "اس عمل کے لیے یہ مرحلہ کیوں ضروری ہے؟", "Is amal ke liye yeh marhala kyun zaroori hai?"),
        ("Could you please write down the spelling for me?", "کیا آپ برائے مہربانی اس کے ہجے لکھ کر دے سکتے ہیں؟", "Kya aap baraye meharbani is ke hijjay likh kar de saktay hain?"),
        ("Is there any exception to this general rule?", "کیا اس عمومی قاعدے کا کوئی استثنیٰ بھی ہے؟", "Kya is umoomi qaaiday ka koi istisna bhi hai?"),
        ("How long do we have before the submission deadline?", "جمع کروانے کی آخری تاریخ تک ہمارے پاس کتنا وقت ہے؟", "Jama karwanay ki aakhri tareekh tak hamare paas kitna waqt hai?"),
        ("What are the prerequisites for joining this course?", "اس کورس میں داخلے کے لیے بنیادی شرائط کیا ہیں؟", "Is course mein dakhlay ke liye bunyadi sharait kya hain?"),
        ("Could you elaborate a bit more on that point?", "کیا آپ اس نکتے پر تھوڑی مزید روشنی ڈال سکتے ہیں؟", "Kya aap is nuktay par thori mazeed roshni daal saktay hain?"),
        ("Am I pronouncing this vowel sound accurately?", "کیا میں اس حرف کی آواز بالکل درست نکال رہا ہوں؟", "Kya mein is harf ki aawaz bilkul durust nikaal raha hoon?"),
        ("Can you walk me through the login process once?", "کیا آپ ایک بار مجھے لاگ ان کا طریقہ سمجھا سکتے ہیں؟", "Kya aap aik baar mujhe login ka tareeqa samjha saktay hain?"),
        ("What is the exact meaning of this technical term?", "اس تکنیکی اصطلاح کا درست مطلب کیا ہے؟", "Is takneeqi istilaah ka durust matlab kya hai?"),
        ("Could you please confirm if my email was received?", "کیا آپ تصدیق کر سکتے ہیں کہ میری ای میل مل گئی تھی؟", "Kya aap tasdeeq kar saktay hain ke meri email mil gayi thi?"),
        ("How does this feature benefit our regular users?", "یہ فیچر ہمارے عام صارفین کو کیسے فائدہ پہنچاتا ہے؟", "Yeh feature hamare aam sarifeen ko kaisay faida pohanchata hai?"),
        ("Which option would you recommend in this situation?", "اس صورتحال میں آپ کون سا آپشن تجویز کریں گے؟", "Is soorathal mein aap kaun sa option tajweez karein ge?"),
        ("Could you please summarize the key takeaways?", "کیا آپ اہم نکات کا خلاصہ پیش کر سکتے ہیں؟", "Kya aap ahem nukat ka khulasa pesh kar saktay hain?"),
        ("Is there anything else I need to keep in mind?", "کیا کوئی اور بات بھی ہے جو مجھے ذہن میں رکھنی چاہیے؟", "Kya koi aur baat bhi hai jo mujhe zehan mein rakhni chahiye?"),
        ("What happens if we miss the scheduled appointment?", "اگر ہم مقررہ وقت پر نہ پہنچ سکے تو کیا ہوگا؟", "Agar hum muqarrara waqt par na pohanch sakay to kya hoga?"),
        ("Can I ask for a second opinion on this diagnosis?", "کیا میں اس تشخیص پر کسی دوسرے ماہر کی رائے لے سکتا ہوں؟", "Kya mein is tashkhees par kisi doosray mahir ki raye le sakta hoon?"),
        ("Could you point out where I made the mistake?", "کیا آپ بتا سکتے ہیں کہ مجھ سے کہاں غلطی ہوئی؟", "Kya aap bata saktay hain ke mujh se kahan ghalti hui?"),
        ("How does this algorithm work behind the scenes?", "یہ الگورتھم پس پردہ کس طرح کام کرتا ہے؟", "Yeh algorithm pas-e-parda kis tarah kaam karta hai?"),
        ("Are these figures inclusive of all taxes?", "کیا ان اعداد و شمار میں تمام ٹیکسز شامل ہیں؟", "Kya in aadaad-o-shumar mein tamam taxes shamil hain?"),
        ("Can you please give me a real-world scenario?", "کیا آپ مجھے حقیقی زندگی کی کوئی مثال دے سکتے ہیں؟", "Kya aap mujhe haqeeqi zindagi ki koi misaal de saktay hain?"),
        ("Why was this particular approach chosen over others?", "دیگر طریقوں کے مقابلے میں یہ خاص طریقہ کیوں چنا گیا؟", "Deegar tareeqon ke muqablay mein yeh khaas tareeqa kyun chuna gaya?"),
        ("Could you please explain it in simpler words?", "کیا آپ اسے مزید آسان الفاظ میں سمجھا سکتے ہیں؟", "Kya aap isay mazeed aasan alfaaz mein samjha saktay hain?"),
        ("Where can I find the official reference guide?", "مجھے سرکاری رہنما کتابچہ کہاں سے مل سکتا ہے؟", "Mujhe sarkari rehnuma kitaabcha kahan se mil sakta hai?"),
        ("What is the next immediate step for our team?", "ہماری ٹیم کے لیے اگلا فوری قدم کیا ہے؟", "Hamari team ke liye agla fauri qadam kya hai?"),
        ("Is this policy applicable to international clients?", "کیا یہ پالیسی بین الاقوامی کلائنٹس پر بھی لاگو ہوتی ہے؟", "Kya yeh policy bain-ul-aqwami clients par bhi laagu hoti hai?"),
        ("Could you please verify these calculations for me?", "کیا آپ برائے مہربانی میرے یہ حساب کتاب چیک کر سکتے ہیں؟", "Kya aap baraye meharbani mere yeh hisab kitab check kar saktay hain?"),
        ("How do we measure the final success of this project?", "ہم اس منصوبے کی حتمی کامیابی کا اندازہ کیسے لگائیں گے؟", "Hum is mansoobay ki hatmi kamyabi ka andaza kaisay lagayein ge?"),
        ("Can you help me understand this paragraph?", "کیا آپ اس پیراگراف کو سمجھنے میں میری مدد کر سکتے ہیں؟", "Kya aap is paragraph ko samajhnay mein meri madad kar saktay hain?"),
        ("What should I do if the system throws an error?", "اگر سسٹم میں خرابی آئے تو مجھے کیا کرنا چاہیے؟", "Agar system mein kharabi aaye to mujhe kya karna chahiye?"),
        ("Are there any hidden charges or extra fees?", "کیا کوئی پوشیدہ اخراجات یا اضافی فیس بھی ہے؟", "Kya koi posheeda ikhrajat ya izafi fees bhi hai?"),
        ("Could you demonstrate how this tool operates?", "کیا آپ دکھا سکتے ہیں کہ یہ ٹول کیسے کام کرتا ہے؟", "Kya aap dikha saktay hain ke yeh tool kaisay kaam karta hai?"),
        ("Where should I submit the final signed form?", "مجھے دستخط شدہ فارم کہاں جمع کروانا چاہیے؟", "Mujhe dastakhat shuda form kahan jama karwana chahiye?"),
        ("What is the estimated turnaround time for approval?", "منظوری میں تقریباً کتنا وقت لگنے کا امکان ہے؟", "Manzoori mein taqreeban kitna waqt lagnay ka imkan hai?"),
        ("Can you clarify what the team's priority is today?", "کیا آپ واضح کر سکتے ہیں کہ آج ٹیم کی ترجیح کیا ہے؟", "Kya aap wazeh kar saktay hain ke aaj team ki tarjeeh kya hai?"),
        ("Is there any update on the issue we reported yesterday?", "کل بتائے گئے مسئلے کے بارے میں کوئی نئی پیش رفت ہے؟", "Kal bataye gaye maslay ke baare mein koi nayi paish-raft hai?"),
        ("Thank you so much for clearing all my doubts.", "میری تمام الجھنیں دور کرنے کا بہت بہت شکریہ۔", "Meri tamam uljhanein door karne ka bohat bohat shukriya."),
        ("Now everything is crystal clear to me.", "اب مجھے ہر چیز بالکل صاف اور واضح سمجھ آ گئی ہے۔", "Ab mujhe har cheez bilkul saaf aur wazeh samajh aa gayi hai.")
    ]
    topics.append({
        "id": 21, "name": "Asking Questions & Clarification", "urduName": "سوالات اور وضاحتیں", "icon": "help",
        "desc": "Clarifying doubts, asking for help, and seeking details.", "sentences": t21
    })

    # 22. Opinions & Agreements
    t22 = [
        ("In my personal opinion, honesty is the greatest virtue.", "میری ذاتی رائے میں سچائی سب سے بڑی خوبی ہے۔", "Meri zaati raye mein sachaai sab se barri khoobi hai."),
        ("I completely agree with your perspective on this matter.", "میں اس معاملے پر آپ کے نقطہ نظر سے مکمل متفق ہوں۔", "Mein is maamlay par aap ke nuqta-e-nazar se mukammal muttafiq hoon."),
        ("I see your point, but I respectfully look at it differently.", "میں آپ کی بات سمجھتا ہوں، لیکن میرا نقطہ نظر تھوڑا مختلف ہے۔", "Mein aap ki baat samajhta hoon, lekin mera nuqta-e-nazar thora mukhtalif hai."),
        ("That sounds like a brilliant and creative solution.", "یہ ایک انتہائی شاندار اور تخلیقی حل لگتا ہے۔", "Yeh aik intehai shandar aur takhleeqi hal lagta hai."),
        ("I am not sure if that would be feasible in practice.", "مجھے یقین نہیں کہ عملی طور پر یہ ممکن ہو سکے گا۔", "Mujhe yaqeen nahi ke amli tor par yeh mumkin ho sakay ga."),
        ("You hit the nail right on the head with that comment.", "آپ نے بالکل درست اور بجا بات کہی ہے۔", "Aap ne bilkul durust aur baja baat kahi hai."),
        ("From my experience, patience always yields great results.", "میرے تجربے کے مطابق صبر ہمیشہ اچھے نتائج دیتا ہے۔", "Mere tajrubay ke mutabiq sabr hamesha achay nataij deta hai."),
        ("I believe teamwork produces far better outcomes than solo work.", "میرا ماننا ہے کہ ٹیم ورک تنہا کام سے بہتر نتائج دیتا ہے۔", "Mera manna hai ke teamwork tanha kaam se behtar nataij deta hai."),
        ("That is exactly what I was going to suggest.", "بالکل یہی بات میں بھی تجویز کرنے والا تھا۔", "Bilkul yahi baat mein bhi tajweez karne wala tha."),
        ("Let us agree to disagree respectfully on this topic.", "آئیں اس موضوع پر باہمی احترام کے ساتھ اختلاف قائم رکھیں۔", "Aayein is mauzo par baahmi ihtiram ke sath ikhtilaaf qayam rakhein."),
        ("What are your thoughts regarding the new proposal?", "نئی تجویز کے بارے میں آپ کی کیا رائے ہے؟", "Nayi tajweez ke baare mein aap ki kya raye hai?"),
        ("I feel that we should invest more in employee training.", "مجھے لگتا ہے کہ ہمیں عملے کی تربیت پر زیادہ سرمایہ لگانا چاہیے۔", "Mujhe lagta hai ke humein amlay ki tarbiyat par zyada sarmaya lagana chahiye."),
        ("I have no doubt that our team will succeed.", "مجھے ذرہ برابر شک نہیں کہ ہماری ٹیم کامیاب ہوگی۔", "Mujhe zarra barabar shak nahi ke hamari team kamyab hogi."),
        ("To be completely honest, I have mixed feelings about this.", "سچ کہوں تو اس بارے میں میرے ملے جلے خیالات ہیں۔", "Sach kahoon to is baare mein mere milay julay khayalaat hain."),
        ("I strongly endorse your proposal for system upgrade.", "میں سسٹم اپ گریڈ کی آپ کی تجویز کی پرزور تائید کرتا ہوں۔", "Mein system upgrade ki aap ki tajweez ki purzor tayeed karta hoon."),
        ("That argument does not sound very convincing to me.", "وہ دلیل مجھے زیادہ وزن دار اور قائل کرنے والی نہیں لگتی۔", "Woh daleel mujhe zyada wazan-daar aur qayil karne wali nahi lagti."),
        ("I think we need to carefully weigh the pros and cons.", "میرے خیال میں ہمیں نفع اور نقصان کا بغور جائزہ لینا چاہیے۔", "Mere khayal mein humein nafa aur nuqsan ka baghaur jaiza lena chahiye."),
        ("There is definitely a lot of merit in your observation.", "آپ کے مشاہدے میں یقیناً بہت وزن اور سچائی ہے۔", "Aap ke mushahiday mein yaqeenan bohat wazan aur sachaai hai."),
        ("I could not have said it any better myself.", "میں خود اس سے بہتر انداز میں بیان نہیں کر سکتا تھا۔", "Mein khud is se behtar andaaz mein bayan nahi kar sakta tha."),
        ("It seems to me that we are rushing this decision.", "مجھے ایسا لگتا ہے کہ ہم یہ فیصلہ کرنے میں جلدی کر رہے ہیں۔", "Mujhe aisa lagta hai ke hum yeh faisla karne mein jaldi kar rahe hain."),
        ("Without question, customer satisfaction should be our goal.", "بلاشبہ، صارفین کا اطمینان ہمارا اولین مقصد ہونا چاہیے۔", "Bila-shuba, sarifeen ka itminaan hamara awaleen maqsad hona chahiye."),
        ("I am inclined to support the second candidate.", "میرا جھکاؤ دوسرے امیدوار کی حمایت کی طرف ہے۔", "Mera jhukao doosray umeedwar ki himayat ki taraf hai."),
        ("Your suggestion makes complete sense in this context.", "اس تناظر میں آپ کا مشورہ بالکل بجا اور منطقی ہے۔", "Is tanazur mein aap ka mashwara bilkul baja aur mantiqi hai."),
        ("I share the exact same sentiment as yours.", "میرے بھی بالکل وہی جذبات اور احساسات ہیں۔", "Mere bhi bilkul wahi jazbaat aur ehsasaat hain."),
        ("We must look at this challenge from multiple angles.", "ہمیں اس چیلنج کو مختلف زاویوں سے دیکھنا چاہیے۔", "Humein is challenge ko mukhtalif zawiyon se dekhna chahiye."),
        ("I respectfully disagree with that particular conclusion.", "میں اس مخصوص نتیجے سے ادب کے ساتھ اختلاف کرتا ہوں۔", "Mein is makhsoos nateejay se adab ke sath ikhtilaaf karta hoon."),
        ("That is a fresh and very intriguing perspective.", "یہ ایک نیا اور انتہائی دلچسپ نقطہ نظر ہے۔", "Yeh aik naya aur intehai dilchasp nuqta-e-nazar hai."),
        ("I am confident that this strategy will pay dividends.", "مجھے یقین ہے کہ یہ حکمت عملی بہترین نتائج لائے گی۔", "Mujhe yaqeen hai ke yeh hikmat-e-amli behtareen nataij laye gi."),
        ("Let us consider the long-term impact of this policy.", "آئیں اس پالیسی کے طویل مدتی اثرات پر غور کریں۔", "Aayein is policy ke taweel muddati asraat par ghaur karein."),
        ("I am convinced that hard work always triumphs.", "مجھے پختہ یقین ہے کہ محنت ہمیشہ رنگ لاتی ہے۔", "Mujhe pukhta yaqeen hai ke mehnat hamesha rang laati hai."),
        ("What is your verdict on this design layout?", "اس ڈیزائن کے لے آؤٹ پر آپ کی کیا حتمی رائے ہے؟", "Is design ke layout par aap ki kya hatmi raye hai?"),
        ("I believe simplicity is always the key to great design.", "میرا ماننا ہے کہ سادگی ہی بہترین ڈیزائن کا راز ہے۔", "Mera manna hai ke saadgi hi behtareen design ka raaz hai."),
        ("I have absolute trust and faith in your leadership.", "مجھے آپ کی قیادت پر پورا بھروسہ اور اعتماد ہے۔", "Mujhe aap ki qayadat par poora bharosa aur aitemad hai."),
        ("That sounds reasonable and fair to all parties.", "یہ تمام فریقین کے لیے مناسب اور منصفانہ لگتا ہے۔", "Yeh tamam fareeqain ke liye munasib aur munsifana lagta hai."),
        ("I think we should listen to what the junior staff says.", "میرے خیال میں ہمیں جونیئر عملے کی بات بھی سننی چاہیے۔", "Mere khayal mein humein junior amlay ki baat bhi sunni chahiye."),
        ("Your feedback was extremely helpful and constructive.", "آپ کی رائے بے حد مفید اور تعمیری تھی۔", "Aap ki raye be-hadd mufeed aur tameeri thi."),
        ("I firmly stand by my earlier statement.", "میں اپنے پچھلے بیان پر پوری طرح قائم ہوں۔", "Mein apne pichlay bayan par poori tarah qayam hoon."),
        ("Let us find common ground where we both agree.", "آئیں ایسا راستہ نکالیں جہاں ہم دونوں متفق ہو سکیں۔", "Aayein aisa rasta nikaalein jahan hum dono muttafiq ho sakein."),
        ("I appreciate your openness to hearing different views.", "مختلف آراء کھلے دل سے سننے پر میں آپ کی قدر کرتا ہوں۔", "Mukhtalif aara khulay dil se sunnay par mein aap ki qadr karta hoon."),
        ("We should never jump to hasty conclusions.", "ہمیں کبھی بھی جلد بازی میں نتائج نہیں نکالنے چاہییں۔", "Humein kabhi bhi jald baazi mein nataij nahi nikaalnay chahiyein."),
        ("I believe education is the foundation of any prosperous nation.", "میرا ماننا ہے کہ تعلیم ہی ہر خوشحال قوم کی بنیاد ہے۔", "Mera manna hai ke taleem hi har khush-haal qaum ki bunyad hai."),
        ("That sounds too optimistic considering our budget.", "ہمارے بجٹ کے پیش نظر یہ کچھ زیادہ ہی پرامید لگتا ہے۔", "Hamare budget ke paish-e-nazar yeh kuch zyada hi pur-umeed lagta hai."),
        ("I think your caution is well-founded and justified.", "میرے خیال میں آپ کی احتیاط بالکل بجا اور جائز ہے۔", "Mere khayal mein aap ki ahtiyat bilkul baja aur jaiz hai."),
        ("We are completely on the same page regarding this.", "ہم اس معاملے میں بالکل ایک ہی رائے رکھتے ہیں۔", "Hum is maamlay mein bilkul aik hi raye rakhtay hain."),
        ("I value your honest opinion above everything else.", "میں ہر چیز سے بڑھ کر آپ کی سچی رائے کی قدر کرتا ہوں۔", "Mein har cheez se barh kar aap ki sachi raye ki qadr karta hoon."),
        ("Let us keep an open mind throughout this discussion.", "آئیں اس بحث کے دوران اپنا ذہن کھلا رکھیں۔", "Aayein is behas ke dauran apna zehan khula rakhein."),
        ("I tend to agree with the majority in this case.", "اس معاملے میں میرا رجحان اکثریت کی رائے کے ساتھ ہے۔", "Is maamlay mein mera rujhaan aksariyat ki raye ke sath hai."),
        ("Every individual has the right to express their thoughts.", "ہر فرد کو اپنی سوچ کا اظہار کرنے کا پورا حق حاصل ہے۔", "Har fard ko apni soch ka izhaar karne ka poora haq haasil hai."),
        ("Mutual respect makes every disagreement productive.", "باہمی احترام ہر اختلاف کو تعمیری بنا دیتا ہے۔", "Baahmi ihtiram har ikhtilaaf ko tameeri bana deta hai."),
        ("Thank you for sharing your thoughtful insights.", "اپنی دانشمندانہ بصیرت شیئر کرنے کا بے حد شکریہ۔", "Apni danishmandana baseerat share karne ka be-hadd shukriya.")
    ]
    topics.append({
        "id": 22, "name": "Opinions & Agreements", "urduName": "آراء اور اتفاقِ رائے", "icon": "psychology",
        "desc": "Expressing personal viewpoints, agreeing, and polite disagreeing.", "sentences": t22
    })

    # We will complete topics 23 to 30
    import part3_topics_23_to_30
    part3_topics_23_to_30.add_part3_23_to_30(topics)

    assert len(topics) == 10
    for t in topics:
        assert len(t["sentences"]) == 50
    return topics

def build():
    dest_dir = "app/src/main/java/com/example/data/model"
    topics = get_part3_topics()
    write_part_file(os.path.join(dest_dir, "DailyConversionPart3.kt"), "DailyConversionPart3", topics)
    print("DailyConversionPart3.kt successfully generated with 500 sentences!")

if __name__ == "__main__":
    build()
