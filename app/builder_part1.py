# -*- coding: utf-8 -*-
import os
from generate_full_database_direct import write_part_file

def build():
    # Topics 1 - 10
    topics = []
    
    # 1. Greetings & Introductions
    topics.append({
        "id": 1, "name": "Greetings & Introductions", "urduName": "خیرمقدم اور تعارف", "icon": "chat",
        "desc": "Daily greetings, meeting people, and polite introductions.",
        "sentences": [
            ("Hello, how are you doing today?", "ہیلو، آپ آج کیسے ہیں؟", "Hello, aap aaj kaise hain?"),
            ("Nice to meet you.", "آپ سے مل کر بہت خوشی ہوئی۔", "Aap se mil kar bohat khushi hui."),
            ("What is your full name?", "آپ کا پورا نام کیا ہے؟", "Aap ka poora naam kya hai?"),
            ("My name is Muhammad Ali.", "میرا نام محمد علی ہے۔", "Mera naam Muhammad Ali hai."),
            ("I am doing great, thank you.", "میں بالکل ٹھیک ہوں، آپ کا شکریہ۔", "Mein bilkul theek hoon, aap ka shukriya."),
            ("Where are you originally from?", "آپ بنیادی طور پر کہاں کے رہنے والے ہیں؟", "Aap bunyadi tor par kahan ke rehne walay hain?"),
            ("I was born in Lahore, Pakistan.", "میں لاہور، پاکستان میں پیدا ہوا تھا۔", "Mein Lahore, Pakistan mein paida hua tha."),
            ("Long time no see! How have you been?", "بہت عرصے بعد ملے! آپ کا کیا حال ہے؟", "Bohat arsay baad milay! Aap ka kya haal hai?"),
            ("Have a wonderful day ahead.", "آپ کا آنے والا دن بہت اچھا گزرے۔", "Aap ka aanay wala din bohat acha guzray."),
            ("It was really great catching up with you.", "آپ سے مل کر اور بات کر کے بہت اچھا لگا۔", "Aap se mil kar aur baat kar ke bohat acha laga."),
            ("Please make yourself at home.", "برائے مہربانی اسے اپنا ہی گھر سمجھیں۔", "Baraye meharbani isay apna hi ghar samjhein."),
            ("Goodbye, take care of yourself!", "اللہ حافظ، اپنا خیال رکھیے گا!", "Allah hafiz, apna khayal rakhiye ga!"),
            ("Good morning, hope you slept well.", "صبح بخیر، امید ہے آپ کی نیند اچھی رہی ہوگی۔", "Subah bakhair, umeed hai aap ki neend achi rahi hogi."),
            ("Good evening, how was your day at work?", "شام بخیر، کام پر آپ کا دن کیسا رہا؟", "Shaam bakhair, kaam par aap ka din kaisa raha?"),
            ("Pleasure to make your acquaintance.", "آپ سے تعارف حاصل کر کے دلی خوشی ہوئی۔", "Aap se taaruf haasil kar ke dili khushi hui."),
            ("Allow me to introduce my colleague.", "مجھے اپنے ساتھی کا تعارف کروانے کی اجازت دیں۔", "Mujhe apne saathi ka taaruf karwanay ki ijazat dein."),
            ("What do you do for a living?", "آپ کیا کام کرتے ہیں؟", "Aap kya kaam kartay hain?"),
            ("I work as a software engineer.", "میں سافٹ ویئر انجینئر کے طور پر کام کرتا ہوں۔", "Mein software engineer ke tor par kaam karta hoon."),
            ("How is everything going with you?", "آپ کے ساتھ سب کیسا چل رہا ہے؟", "Aap ke sath sab kaisa chal raha hai?"),
            ("Everything is going smoothly, Alhamdulillah.", "الحمدللہ سب کچھ بہت اچھا چل رہا ہے۔", "Alhamdulillah sab kuch bohat acha chal raha hai."),
            ("I would like to welcome you warmly.", "میں آپ کو دل سے خوش آمدید کہتا ہوں۔", "Mein aap ko dil se khush aamdeed kehta hoon."),
            ("Thank you for taking the time to meet me.", "مجھ سے ملنے کے لیے وقت نکالنے کا شکریہ۔", "Mujh se milnay ke liye waqt nikaalnay ka shukriya."),
            ("Have we met somewhere before?", "کیا ہم پہلے کہیں مل چکے ہیں؟", "Kya hum pehle kahin mil chukay hain?"),
            ("Yes, I think we met at the conference.", "جی ہاں، میرے خیال میں ہم کانفرنس میں ملے تھے۔", "Jee haan, mere khayal mein hum conference mein milay thay."),
            ("Please give my warm regards to your parents.", "اپنے والدین کو میرا سلام کہیے گا۔", "Apne walidain ko mera salam kahiye ga."),
            ("It is an honor to meet you in person.", "آپ سے بالمشافہ ملنا میرے لیے باعث فخر ہے۔", "Aap se bil-mushafaha milna mere liye baais-e-fakhar hai."),
            ("How is your whole family doing?", "آپ کے اہل خانہ کی طبیعت کیسی ہے؟", "Aap ke ahl-e-khana ki tabiat kaisi hai?"),
            ("They are all in good health, thank you.", "وہ سب بالکل خیریت سے ہیں، شکریہ۔", "Woh sab bilkul khairiyat se hain, shukriya."),
            ("Feel free to contact me whenever you need.", "جب بھی ضرورت ہو بلا جھجھک رابطہ کریں۔", "Jab bhi zaroorat ho bila jhijhak rabta karein."),
            ("I really appreciate your kind support.", "میں آپ کے تعاون کا بے حد مشکور ہوں۔", "Mein aap ke ta'awun ka be-hadd mashkoor hoon."),
            ("Let's keep in touch through WhatsApp.", "آئیں واٹس ایپ پر رابطے میں رہتے ہیں۔", "Aayein WhatsApp par rabtay mein rehtay hain."),
            ("Here is my business contact card.", "یہ میرا وزٹنگ کارڈ ہے۔", "Yeh mera visiting card hai."),
            ("I am glad to welcome you here.", "مجھے یہاں آپ کا خیرمقدم کر کے خوشی ہوئی۔", "Mujhe yahan aap ka khair-muqaddam kar ke khushi hui."),
            ("How long have you lived in this town?", "آپ اس قصبے میں کتنے عرصے سے رہ رہے ہیں؟", "Aap is qasbay mein kitnay arsay se reh rahe hain?"),
            ("I moved here about three years ago.", "میں تقریباً تین سال پہلے یہاں منتقل ہوا تھا۔", "Mein taqreeban teen saal pehle yahan muntaqil hua tha."),
            ("I hope you have a pleasant day.", "امید ہے آپ کا دن خوشگوار گزرے گا۔", "Umeed hai aap ka din khushgawar guzray ga."),
            ("This is my close friend, Tariq.", "یہ میرا قریبی دوست طارق ہے۔", "Yeh mera qareebi dost Tariq hai."),
            ("See you soon, take care!", "جلد دوبارہ ملتے ہیں، خیال رکھیے گا!", "Jald dobara miltay hain, khayal rakhiye ga!"),
            ("Good night and have sweet dreams.", "شب بخیر اور میٹھے خواب دیکھیں۔", "Shab bakhair aur meethay khwab dekhein."),
            ("Thank you for your valuable advice.", "آپ کے قیمتی مشورے کا شکریہ۔", "Aap ke qeemti mashwaray ka shukriya."),
            ("I am honored by your kind words.", "آپ کے اچھے الفاظ پر میں ممنون ہوں۔", "Aap ke achay alfaaz par mein mamnoon hoon."),
            ("Please excuse me for being a bit late.", "تھوڑی تاخیر کے لیے معذرت خواہ ہوں۔", "Thori taakheer ke liye ma'azrat khwah hoon."),
            ("No worries at all, it's fine.", "کوئی بات نہیں، بالکل ٹھیک ہے۔", "Koi baat nahi, bilkul theek hai."),
            ("It was delightful talking to you.", "آپ سے بات چیت کر کے بہت خوشی ہوئی۔", "Aap se baat cheet kar ke bohat khushi hui."),
            ("What are your plans for the weekend?", "ویک اینڈ پر آپ کا کیا ارادہ ہے؟", "Weekend par aap ka kya irada hai?"),
            ("I plan to stay home and rest.", "میرا ارادہ گھر رہ کر آرام کرنے کا ہے۔", "Mera irada ghar reh kar aaram karne ka hai."),
            ("I look forward to meeting you again.", "مجھے آپ سے دوبارہ ملنے کی امید رہے گی۔", "Mujhe aap se dobara milnay ki umeed rahay gi."),
            ("Please pass my congratulations to your brother.", "اپنے بھائی کو میری طرف سے مبارکباد دیجیے گا۔", "Apne bhai ko meri taraf se mubarakbaad dijiye ga."),
            ("May you achieve great success in life.", "اللہ آپ کو زندگی میں بڑی کامیابی عطا کرے۔", "Allah aap ko zindagi mein barri kamyabi ata karay."),
            ("Thank you so much, wish you the best!", "بہت بہت شکریہ، آپ کے لیے نیک خواہشات!", "Bohat bohat shukriya, aap ke liye naik khwahishaat!")
        ]
    })

    # 2. Family & Relationships
    topics.append({
        "id": 2, "name": "Family & Relationships", "urduName": "خاندان اور تعلقات", "icon": "people",
        "desc": "Talk about parents, siblings, children, and relatives.",
        "sentences": [
            ("How many members are there in your family?", "آپ کے خاندان میں کتنے افراد ہیں؟", "Aap ke khandan mein kitnay afraad hain?"),
            ("I live with my parents and younger brother.", "میں اپنے والدین اور چھوٹے بھائی کے ساتھ رہتا ہوں۔", "Mein apne walidain aur chhotay bhai ke sath rehta hoon."),
            ("She resembles her mother very closely.", "وہ اپنی والدہ سے بہت زیادہ مشابہت رکھتی ہے۔", "Woh apni walida se bohat zyada mushabahat rakhti hai."),
            ("My elder sister is studying medicine abroad.", "میری بڑی بہن بیرون ملک میڈیکل پڑھ رہی ہے۔", "Meri barri behan bairoon-e-mulk medical parh rahi hai."),
            ("We always have dinner together as a family.", "ہم ہمیشہ خاندان کے ساتھ مل کر کھانا کھاتے ہیں۔", "Hum hamesha khandan ke sath mil kar khana khatay hain."),
            ("My grandfather tells fascinating bedtime stories.", "میرے دادا جان سونے سے پہلے دلچسپ کہانیاں سناتے ہیں۔", "Mere dada jaan sonay se pehle dilchasp kahaniyan sunatay hain."),
            ("Do you have any brothers or sisters?", "کیا آپ کا کوئی بھائی یا بہن ہے؟", "Kya aap ka koi bhai ya behan hai?"),
            ("Family support is the greatest strength in life.", "خاندان کا ساتھ زندگی میں سب سے بڑی طاقت ہے۔", "Khandan ka sath zindagi mein sab se barri taaqat hai."),
            ("My uncle invited us over for lunch this Sunday.", "میرے چچا نے ہمیں اتوار کو کھانے کی دعوت دی ہے۔", "Mere chacha ne humein itwar ko khanay ki daawat di hai."),
            ("We celebrate every Eid together with happiness.", "ہم ہر عید خوشی کے ساتھ مل جل کر مناتے ہیں۔", "Hum har Eid khushi ke sath mil jul kar manatay hain."),
            ("My parents have been married for thirty years.", "میرے والدین کی شادی کو تیس سال ہو چکے ہیں۔", "Mere walidain ki shaadi ko tees saal ho chukay hain."),
            ("I have two elder brothers and one younger sister.", "میرے دو بڑے بھائی اور ایک چھوٹی بہن ہے۔", "Mere do baray bhai aur aik chhoti behan hai."),
            ("My mother cooks the most delicious homemade food.", "میری والدہ گھر کا انتہائی لذیذ کھانا بناتی ہیں۔", "Meri walida ghar ka intehai lazeez khana banati hain."),
            ("My father works hard to provide for us.", "میرے والد ہماری دیکھ بھال کے لیے سخت محنت کرتے ہیں۔", "Mere walid hamari dekh bhaal ke liye sakht mehnat kartay hain."),
            ("We visited our ancestral village during holidays.", "ہم نے چھٹیوں میں اپنے آبائی گاؤں کا دورہ کیا۔", "Hum ne chuttiyon mein apne aabai gaanv ka daura kiya."),
            ("My grandmother loves gardening in her backyard.", "میری دادی کو باغیچے کی دیکھ بھال پسند ہے۔", "Meri dadi ko bagheeche ki dekh bhaal pasand hai."),
            ("We respect our elders and cherish their wisdom.", "ہم اپنے بزرگوں کا احترام کرتے ہیں اور ان کی رہنمائی کی قدر کرتے ہیں۔", "Hum apne bazurgon ka ihtiram kartay hain aur un ki rehnumai ki qadr kartay hain."),
            ("My cousins are visiting us from Islamabad.", "میرے کزنز اسلام آباد سے ہمارے گھر آ رہے ہیں۔", "Mere cousins Islamabad se hamare ghar aa rahe hain."),
            ("Who is the oldest sibling in your household?", "آپ کے گھر میں سب سے بڑا بہن بھائی کون ہے؟", "Aap ke ghar mein sab se barra behan bhai kaun hai?"),
            ("I am the youngest child in my family.", "میں اپنے خاندان میں سب سے چھوٹا بچہ ہوں۔", "Mein apne khandan mein sab se chhota bacha hoon."),
            ("We take care of each other through thick and thin.", "ہم اچھے برے ہر وقت میں ایک دوسرے کا خیال رکھتے ہیں۔", "Hum achay buray har waqt mein aik doosray ka khayal rakhtay hain."),
            ("My aunt sent delicious homemade mango pickles.", "میری خالہ نے آم کا مزیدار گھریلو اچار بھیجا ہے۔", "Meri khala ne aam ka mazedar gharelu achaar bheja hai."),
            ("Family bonding makes our home full of peace.", "خاندانی محبت ہمارے گھر کو پرامن بناتی ہے۔", "Khandani mohabbat hamare ghar ko pursukoon banati hai."),
            ("My nephew is learning how to walk.", "میرا بھتیجا اب چلنا سیکھ رہا ہے۔", "Mera bhateeja ab chalna seekh raha hai."),
            ("We organized a small family reunion last month.", "ہم نے پچھلے مہینے خاندان کا ایک چھوٹا اجتماع رکھا۔", "Hum ne pichlay maheenay khandan ka aik chhota ijtima rakha."),
            ("Children bring joy and laughter to every home.", "بچے ہر گھر میں خوشیاں اور رونق لاتے ہیں۔", "Bachay har ghar mein khushiyan aur raunaq laatay hain."),
            ("My sister graduated with top honors from college.", "میری بہن نے کالج میں پہلی پوزیشن حاصل کی۔", "Meri behan ne college mein pehli position haasil ki."),
            ("My brother helped me repair my broken laptop.", "میرے بھائی نے میرا لیپ ٹاپ ٹھیک کرنے میں مدد کی۔", "Mere bhai ne mera laptop theek karne mein madad ki."),
            ("We should always honor and serve our parents.", "ہمیں ہمیشہ اپنے والدین کی خدمت اور عزت کرنی چاہیے۔", "Humein hamesha apne walidain ki khidmat aur izzat karni chahiye."),
            ("Grandparents give unconditional love to children.", "دادا دادی بچوں سے بے لوث محبت کرتے ہیں۔", "Dada dadi bachon se be-laus mohabbat kartay hain."),
            ("I learned good values and manners from my family.", "میں نے اپنے خاندان سے اچھی تربیت اور اخلاق سیکھے۔", "Mein ne apne khandan se achi tarbiyat aur ikhlaaq seekhay."),
            ("My cousin got engaged last Friday.", "میری کزن کی منگنی پچھلے جمعہ کو ہوئی۔", "Meri cousin ki mangni pichlay jumma ko hui."),
            ("We have a big family gathering on weekends.", "ویک اینڈ پر ہماری بڑی فیملی بیٹھک ہوتی ہے۔", "Weekend par hamari barri family baithak hoti hai."),
            ("Parental blessings are the key to true success.", "والدین کی دعائیں حقیقی کامیابی کی کلید ہیں۔", "Walidain ki duayein haqeeqi kamyabi ki kaleed hain."),
            ("My little niece loves coloring storybooks.", "میری چھوٹی بھانجی کہانیوں کی کتابوں میں رنگ بھرنا پسند کرتی ہے۔", "Meri chhoti bhaanji kahaniyon ki kitabon mein rang bharna pasand karti hai."),
            ("We share our joys and sorrows with one another.", "ہم اپنے دکھ سکھ ایک دوسرے کے ساتھ بانٹتے ہیں۔", "Hum apne dukh sukh aik doosray ke sath baant-tay hain."),
            ("My brother is preparing for his board exams.", "میرا بھائی اپنے بورڈ کے امتحانات کی تیاری کر رہا ہے۔", "Mera bhai apne board ke imtihanaat ki tayari kar raha hai."),
            ("We love having afternoon tea together on the terrace.", "ہمیں چھت پر شام کی چائے ساتھ پینا پسند ہے۔", "Humein chhat par shaam ki chaye sath peena pasand hai."),
            ("A united family can overcome any challenge.", "ایک متحد خاندان ہر مشکل کا آسانی سے مقابلہ کر سکتا ہے۔", "Aik muttahid khandan har mushkil ka aasani se muqabla kar sakta hai."),
            ("My uncle lives in the United Kingdom.", "میرے چچا برطانیہ میں مقیم ہیں۔", "Mere chacha Bartania mein muqeem hain."),
            ("We always remember our ancestors with prayer.", "ہم اپنے آباؤ اجداد کو ہمیشہ دعاؤں میں یاد رکھتے ہیں۔", "Hum apne aaba-o-ajdaad ko hamesha duaon mein yaad rakhtay hain."),
            ("Family traditions keep our cultural roots strong.", "خاندانی روایات ہماری ثقافت کو زندہ رکھتی ہیں۔", "Khandani riwayaat hamari saqafat ko zinda rakhti hain."),
            ("My mother taught me the value of honesty.", "میری ماں نے مجھے ایمانداری کی اہمیت سکھائی۔", "Meri maa ne mujhe imandari ki ahmiyat sikhayi."),
            ("My father is a very patient and wise man.", "میرے والد ایک بہت صابر اور عقلمند انسان ہیں۔", "Mere walid aik bohat saabir aur aqlmand insaan hain."),
            ("We take evening walks together in the park.", "ہم شام کو پارک میں ساتھ چہل قدمی کرتے ہیں۔", "Hum shaam ko park mein sath chehal-qadmi kartay hain."),
            ("Sibling love is one of life's purest gifts.", "بہن بھائیوں کی محبت زندگی کا انمول تحفہ ہے۔", "Behan bhaiyon ki mohabbat zindagi ka anmol tohfa hai."),
            ("My family always encourages me to do my best.", "میرا خاندان ہمیشہ مجھے بہترین کارکردگی کی ترغیب دیتا ہے۔", "Mera khandan hamesha mujhe behtareen karkardagi ki targheeb deta hai."),
            ("We cherish old family photographs and memories.", "ہم خاندانی پرانی تصاویر اور یادوں کو سنبھال کر رکھتے ہیں۔", "Hum khandani purani tasaveer aur yaadon ko sambhal kar rakhtay hain."),
            ("Peace at home leads to peace in the entire world.", "گھر کا سکون ہی دنیا کے امن کا راستہ ہے۔", "Ghar ka sukoon hi dunya ke aman ka rasta hai."),
            ("I am truly blessed to have such a supportive family.", "ایسا محبت کرنے والا خاندان ملنا میری خوش قسمتی ہے۔", "Aisa mohabbat karne wala khandan milna meri khush-qismati hai.")
        ]
    })

    # Let's add topics 3 through 10
    import part1_topics_3_to_10
    part1_topics_3_to_10.add_remaining_topics(topics)

    assert len(topics) == 10
    for t in topics:
        assert len(t["sentences"]) == 50, f"Topic {t['id']} has {len(t['sentences'])} sentences!"
        
    dest_dir = "/app/src/main/java/com/example/data/model"
    write_part_file(os.path.join(dest_dir, "DailyTopicSentencesPart1.kt"), "DailyTopicSentencesPart1", topics)
    print("Part 1 built successfully (500 sentences across 10 topics).")

if __name__ == "__main__":
    build()
