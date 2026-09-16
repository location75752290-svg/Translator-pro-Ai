# -*- coding: utf-8 -*-

def add_25_to_30(topics):
    # 25. Movies & Entertainment
    t25 = [
        ("Have you watched the latest historical movie release?", "کیا آپ نے حال ہی میں ریلیز ہونے والی تاریخی فلم دیکھی ہے؟", "Kya aap ne haal hi mein release honay wali tareekhi film dekhi hai?"),
        ("The plot twist at the climax of the film left me stunned.", "فلم کے کلائمیکس پر آنے والے موڑ نے مجھے حیران کر دیا۔", "Film ke climax par aanay walay morr ne mujhe heran kar diya."),
        ("Who is your all-time favorite dramatic actor?", "آپ کا اب تک کا سب سے پسندیدہ ڈرامائی اداکار کون ہے؟", "Aap ka ab tak ka sab se pasandeeda dramaee adakaar kaun hai?"),
        ("We reserved front-row cinema tickets for the evening show.", "ہم نے شام کے شو کے لیے سنیما کی اگلی نشستیں بک کروائیں۔", "Hum ne shaam ke show ke liye cinema ki agli nishastein book karwayein."),
        ("The visual special effects and CGI were truly world class.", "فلم کے بصری اثرات (اسپیشل ایفیکٹس) واقعی بین الاقوامی معیار کے تھے۔", "Film ke basri asraat (special effects) waqai bain-ul-aqwami meyaar ke thay."),
        ("That family comedy series had us laughing non-stop.", "اس خاندانی مزاحیہ ڈرامے نے ہمیں مسلسل ہنسنے پر مجبور کیا۔", "Is khandani mizahiya drame ne humein musalsal hansnay par majboor kiya."),
        ("The movie soundtrack and orchestral background score were sublime.", "فلم کا بیک گراؤنڈ میوزک اور گانے لاجواب تھے۔", "Film ka background music aur gaanay la-jawab thay."),
        ("Is this animated film suitable for young kids to watch?", "کیا یہ اینیمیٹڈ فلم چھوٹے بچوں کے دیکھنے کے لیے مناسب ہے؟", "Kya yeh animated film chhotay bachon ke dekhnay ke liye munasib hai?"),
        ("I enjoy binge-watching suspenseful thriller mystery series.", "مجھے سسپنس اور سنسنی خیز پراسرار سیریز دیکھنا پسند ہے۔", "Mujhe suspense aur sansani khez pur-asraar series dekhna pasand hai."),
        ("The lead actress delivered an emotionally powerhouse performance.", "مرکزی اداکارہ نے انتہائی پرتاثیر اور جاندار اداکاری کی۔", "Markazi adakaara ne intehai pur-taseer aur jaandaar adakari ki."),
        ("What genre of movies do you enjoy watching most?", "آپ کس قسم (شعبے) کی فلمیں دیکھنا زیادہ پسند کرتے ہیں؟", "Aap kis qism (sho'bay) ki filmein dekhna zyada pasand kartay hain?"),
        ("I prefer science fiction and historical docudramas.", "میں سائنس فکشن اور تاریخی دستاویزی فلمیں ترجیح دیتا ہوں۔", "Mein science fiction aur tareekhi dastaweezi filmein tarjeeh deta hoon."),
        ("The cinema hall was equipped with Dolby Atmos surround sound.", "سنیما ہال ڈولبی ایٹموس ساؤنڈ سسٹم سے لیس تھا۔", "Cinema hall Dolby Atmos sound system se lais tha."),
        ("Let's get a large bucket of salted butter popcorn.", "آئیں مکھن والے پاپ کارن کا ایک بڑا ٹب لے لیتے ہیں۔", "Aayein makkhan walay popcorn ka aik barra tub le letay hain."),
        ("The director captured the scenic landscapes with poetic beauty.", "ہدایت کار نے قدرتی مناظر کو شاعرانہ خوبصورتی کے ساتھ فلمایا۔", "Hidayat-kaar ne qudrati manazir ko shairana khubsurti ke sath filmaya."),
        ("Have you seen the preview trailer for the upcoming sequel?", "کیا آپ نے آنے والے اگلے حصے کا ٹریلر دیکھا ہے؟", "Kya aap ne aanay walay aglay hissay ka trailer dekha hai?"),
        ("The theatre play received a standing ovation from the audience.", "تھیٹر کے کھیل کو تمام ناظرین کی طرف سے کھڑے ہو کر داد ملی۔", "Theater ke khel ko tamam naazireen ki taraf se kharray ho kar daad mili."),
        ("I love watching witty stand-up comedy specials on weekends.", "مجھے ویک اینڈ پر برجستہ مزاحیہ شوز دیکھنا پسند ہے۔", "Mujhe weekend par barjasta mizahiya shows dekhna pasand hai."),
        ("The scriptwriting was exceptionally witty and meaningful.", "فلم کی کہانی اور مکالمے غیر معمولی طور پر بامعنی اور پرمزاح تھے۔", "Film ki kahani aur mukalmay ghair mamooli tor par ba-maani thay."),
        ("The movie adaptation stayed very loyal to the original book.", "فلم کتاب کے اصل متن اور روح کے بالکل قریب رہی۔", "Film kitaab ke asal matan aur rooh ke bilkul qareeb rahi."),
        ("Which online streaming platform do you subscribe to?", "آپ کس آن لائن اسٹریمنگ پلیٹ فارم کے سبسکرائبر ہیں؟", "Aap kis online streaming platform ke subscriber hain?"),
        ("I love watching classic Pakistani television dramas from the 90s.", "مجھے نوے کی دہائی کے کلاسک پاکستانی ڈرامے دیکھنا پسند ہے۔", "Mujhe 90s ki dahai ke classic Pakistani drame dekhna pasand hai."),
        ("The lead protagonist faces intense moral dilemmas throughout.", "مرکزی کردار کو کہانی میں شدید اخلاقی کشمکش کا سامنا رہتا ہے۔", "Markazi kirdaar ko kahani mein shadeed ikhlaaqi kashmakash ka samna rehta hai."),
        ("We should avoid watching violent cinema before bedtime.", "ہمیں سونے سے پہلے پرتشدد فلمیں دیکھنے سے پرہیز کرنا چاہیے۔", "Humein sonay se pehle pur-tashaddud filmein dekhnay se parhez karna chahiye."),
        ("The film won three prestigious international academy awards.", "فلم نے تین معزز بین الاقوامی اکیڈمی ایوارڈز جیتے۔", "Film ne teen moazzaz bain-ul-aqwami academy awards jeetay."),
        ("The dialogue delivery of the villain gave me goosebumps.", "ولن کے مکالموں کی ادائیگی نے رونگٹے کھڑے کر دیے۔", "Villain ke mukalmon ki adayigi ne rongtay kharray kar diye."),
        ("Do you prefer watching movies at the cinema or at home?", "کیا آپ سنیما میں فلم دیکھنا پسند کرتے ہیں یا گھر پر؟", "Kya aap cinema mein film dekhna pasand kartay hain ya ghar par?"),
        ("I love the immersive giant screen experience of IMAX.", "مجھے آئی میکس کی بڑی اسکرین کا سحر انگیز تجربہ پسند ہے۔", "Mujhe IMAX ki barri screen ka sehar-angeez tajruba pasand hai."),
        ("The documentary shed light on pressing environmental issues.", "دستاویزی فلم نے ماحولیات کے اہم مسائل پر روشنی ڈالی۔", "Dastaweezi film ne maholiyaat ke ahem masail par roshni daali."),
        ("A good story reflects human emotions, struggles, and hope.", "ایک اچھی کہانی انسانی جذبات، جدوجہد اور امید کا آئینہ دار ہوتی ہے۔", "Aik achi kahani insani jazbaat, jadd-o-jehad aur umeed ka aaina-daar hoti hai."),
        ("The cinematography in that desert scene was breathtaking.", "اس صحرائی منظر کی فوٹوگرافی دل دہلا دینے کی حد تک خوبصورت تھی۔", "Us sehrai manzar ki photography dil dehla denay ki hadd tak khubsurat thi."),
        ("He is a versatile actor capable of playing any challenging role.", "وہ ایک ہمہ جہت اداکار ہے جو ہر مشکل کردار نبھا سکتا ہے۔", "Woh aik hama-jehat adakaar hai jo har mushkil kirdaar nibha sakta hai."),
        ("The film ended on an ambiguous, thought-provoking note.", "فلم کا اختتام ایک سوچ طلب اور معنی خیز موڑ پر ہوا۔", "Film ka ikhtitaam aik soch-talab aur maani-khez morr par hua."),
        ("We had a lovely family movie night with homemade pizza.", "ہم نے گھر کے پیزا کے ساتھ فیملی مووی نائٹ کا خوب لطف اٹھایا۔", "Hum ne ghar ke pizza ke sath family movie night ka khoob lutf uthaya."),
        ("The comedy actor has an impeccable sense of comic timing.", "مزاحیہ اداکار کا وقت کا چناؤ اور انداز بے مثال ہے۔", "Mizahiya adakaar ka waqt ka chunao aur andaaz be-misaal hai."),
        ("Children loved the colorful magical fantasy adventure.", "بچوں کو رنگ برنگے جادوئی مناظر والی مہم جوئی بہت پسند آئی۔", "Bachon ko rang barange jadooi manazir wali muhim-jooi bohat pasand aayi."),
        ("The costume design perfectly captured the Mughal royal era.", "لباس کے ڈیزائن نے مغل دور کی شاہی جھلکیاں شاندار انداز میں پیش کیں۔", "Libaas ke design ne Mughal daur ki shahi jhalakiyan shandar andaaz mein pesh keen."),
        ("I was on the edge of my seat throughout the thriller.", "سسپنس فلم کے دوران میں تجسس سے اپنی سیٹ کے کونے پر بیٹھا رہا۔", "Suspense film ke dauran mein tajassus se apni seat ke konay par baitha raha."),
        ("Constructive film criticism elevates artistic storytelling.", "تعمیری تنقید کہانی سنانے کے فن کو نکھار دیتی ہے۔", "Tameeri tanqeed kahani sunanay ke fann ko nikhaar deti hai."),
        ("Let's watch a light-hearted romantic comedy tonight.", "آئیں آج رات کوئی ہلکی پھلکی رومانوی مزاحیہ فلم دیکھتے ہیں۔", "Aayein aaj raat koi halki phulki romanwi mizahiya film dekhtay hain."),
        ("The movie set recreated ancient Lahore with stunning accuracy.", "فلم کے سیٹ پر قدیم لاہور کو انتہائی خوبصورتی سے سجایا گیا۔", "Film ke set par qadeem Lahore ko intehai khubsurti se sajaya gaya."),
        ("He wrote, produced, directed, and acted in his own film.", "اس نے اپنی فلم خود لکھی، بنائی، ڈائریکٹ کی اور اس میں اداکاری کی۔", "Us ne apni film khud likhi, banayi, direct ki aur us mein adakari ki."),
        ("The audience clapped enthusiastically during the victory scene.", "کامیابی کے منظر پر ہال میں موجود لوگوں نے پرجوش تالیاں بجائیں۔", "Kamyabi ke manzar par hall mein maujood logon ne purjosh taaliyan bajayein."),
        ("It is inspiring to see young filmmakers produce original work.", "نوجوان فلم سازوں کا نیا کام بناتے دیکھنا دل کو حوصلہ دیتا ہے۔", "Naujawan film saazon ka naya kaam banatay dekhna dil ko hausla deta hai."),
        ("The film highlighted important social issues with dignity.", "فلم نے اہم سماجی مسائل کو وقار اور سلیقے کے ساتھ اجاگر کیا۔", "Film ne ahem samaji masail ko waqaar aur saleeqay ke sath ujagar kiya."),
        ("Cinema bridges cultures by connecting shared human hearts.", "سنیما مختلف تہذیبوں کو دل کے رشتے کے ذریعے قریب لاتا ہے۔", "Cinema mukhtalif tehzeebon ko dil ke rishtay ke zariye qareeb laata hai."),
        ("Don't spoil the thrilling ending for those who haven't watched!", "جنہوں نے نہیں دیکھی ان کے لیے سسپنس والا اختتام مت بتائیں!", "Jinhon ne nahi dekhi un ke liye suspense wala ikhtitaam mat batayein!"),
        ("The movie ticket prices are discounted on Tuesday afternoons.", "منگل کی دوپہر کو فلم کے ٹکٹوں کی قیمتیں کم ہوتی ہیں۔", "Mangal ki dopahar ko film ke ticketon ki qeematein kam hoti hain."),
        ("Great art entertains, enlightens, and inspires the human spirit.", "عظیم فن تفریح بھی دیتا ہے اور انسان کی روح کو بھی جگاتا ہے۔", "Azeem fann tafreeh bhi deta hai aur insaan ki rooh ko bhi jagata hai."),
        ("Wishing you an entertaining and enjoyable cinematic evening!", "آپ کی فلمی شام خوشگوار، پرلطف اور شاندار گزرے!", "Aap ki filmi shaam khushgawar, pur-lutf aur shandar guzray!")
    ]
    topics.append({
        "id": 25, "name": "Movies & Entertainment", "urduName": "فلمیں اور تفریح", "icon": "whatshot",
        "desc": "Watching cinema, comedy shows, dramas, and entertainment.", "sentences": t25
    })

    # Add 26 to 30
    import part3_topics_26_to_30
    part3_topics_26_to_30.add_26_to_30(topics)

print("Part 3 25-30 loaded.")
