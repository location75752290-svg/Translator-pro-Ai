package com.example.data.model

data class RuleExample(
    val english: String,
    val urdu: String,
    val romanUrdu: String = ""
)

data class MCQ(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class GrammarRule(
    val id: Int,
    val title: String,
    val titleUrdu: String,
    val category: String, // "Modals", "Tenses", "Grammar"
    val iconName: String, // e.g. "star", "build", "book", "school", "pets", etc.
    val explanation: String,
    val explanationUrdu: String,
    val formula: String,
    val examples: List<RuleExample>,
    val quiz: List<MCQ>
)

object LearningToolsDatabase {
    val rules: List<GrammarRule> = listOf(
        // 1. CAN
        GrammarRule(
            id = 1,
            title = "Modal Verb: Can",
            titleUrdu = "Can کا استعمال",
            category = "Modals",
            iconName = "whatshot",
            explanation = "Used to express present ability, capacity, permission, or a general possibility.",
            explanationUrdu = "موجودہ دور کی صلاحیت، قابلیت، اجازت یا عام امکان کے اظہار کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + can + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("I can speak English fluently.", "میں روانی سے انگریزی بول سکتا ہوں۔"),
                RuleExample("Can I borrow your pen for a minute?", "کیا میں ایک منٹ کے لیے آپ کا قلم ادھار لے سکتا ہوں؟"),
                RuleExample("She can solve this difficult math puzzle.", "وہ ریاضی کا یہ مشکل معمہ حل کر سکتی ہے۔"),
                RuleExample("Water can boil at 100 degrees Celsius.", "پانی 100 ڈگری سیلسیس پر ابل سکتا ہے۔"),
                RuleExample("You can park your car outside the gate.", "آپ اپنی گاڑی گیٹ کے باہر کھڑی کر سکتے ہیں۔")
            ),
            quiz = listOf(
                MCQ("He ______ run five miles without stopping.", listOf("can", "may", "must to", "can to"), 0, "Use 'can' for general present physical ability."),
                MCQ("______ I use your phone, please?", listOf("Should", "Can", "Will", "Would"), 1, "Use 'Can' for informal permission."),
                MCQ("They ______ speak Urdu very well.", listOf("can", "can to", "could to", "cans"), 0, "No 'to' or 's' is used after modal verbs."),
                MCQ("I ______ find my keys anywhere.", listOf("cannot", "not can", "can to not", "dont can"), 0, "'cannot' is the standard negative form."),
                MCQ("______ you swim across this wide river?", listOf("May", "Should", "Can", "Will"), 2, "Use 'Can' for physical ability."),
                MCQ("She ______ play the piano since she was five.", listOf("can", "could", "may", "will"), 0, "Use 'can' for ongoing ability."),
                MCQ("Smoking ______ cause serious health issues.", listOf("should", "must to", "can", "would"), 2, "Use 'can' for general possibility."),
                MCQ("You ______ access the internet here.", listOf("can", "should to", "could to", "might to"), 0, "Use 'can' for permission or opportunity."),
                MCQ("He ______ ride a horse easily.", listOf("cans", "can", "can to", "could to"), 1, "Modal verbs are invariant."),
                MCQ("We ______ start the meeting now.", listOf("can", "shall to", "must to", "will to"), 0, "Use 'can' to indicate current opportunity.")
            )
        ),
        // 2. COULD
        GrammarRule(
            id = 2,
            title = "Modal Verb: Could",
            titleUrdu = "Could کا استعمال",
            category = "Modals",
            iconName = "checkroom",
            explanation = "Used as the past tense of 'can' for past ability, or for making polite requests and suggestions.",
            explanationUrdu = "ماضی کی صلاحیت کے لیے، یا شائستہ اور رسمی درخواستیں کرنے کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + could + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("When I was young, I could run fast.", "جب میں جوان تھا، تو میں تیز دوڑ سکتا تھا۔"),
                RuleExample("Could you please open the window?", "کیا آپ مہربانی کر کے کھڑکی کھول سکتے ہیں؟"),
                RuleExample("We could go to the park if you want.", "اگر آپ چاہیں تو ہم پارک جا سکتے ہیں۔"),
                RuleExample("I could not finish the exam on time.", "میں وقت پر امتحان ختم نہیں کر سکا۔"),
                RuleExample("Could I speak to the manager, please?", "کیا میں مینیجر سے بات کر سکتا ہوں، براہ کرم؟")
            ),
            quiz = listOf(
                MCQ("When he was young, he ______ climb any tree.", listOf("can", "could", "should", "must"), 1, "Use 'could' for past ability."),
                MCQ("______ you pass the salt, please?", listOf("Could", "Should", "Might", "Shall"), 0, "'Could' is used for polite requests."),
                MCQ("She ______ read without glasses ten years ago.", listOf("can", "could", "should", "may"), 1, "Use 'could' for past ability."),
                MCQ("If I had more money, I ______ buy a car.", listOf("can", "could", "must", "should"), 1, "Use 'could' for hypothetical ability."),
                MCQ("They ______ not catch the bus yesterday.", listOf("could", "can", "should", "will"), 0, "Use 'could not' for failure to do something in the past."),
                MCQ("______ I borrow your pen for a second?", listOf("Could", "Would", "Should", "Must"), 0, "Polite request is made with 'Could'."),
                MCQ("He said that he ______ solve the puzzle.", listOf("can", "could", "will", "may"), 1, "Use 'could' in indirect speech representing 'can'."),
                MCQ("We ______ go to the cinema tonight if you're free.", listOf("could", "must to", "should to", "could to"), 0, "Use 'could' for suggestions."),
                MCQ("I ______ hear a strange noise in the kitchen.", listOf("could", "would to", "should to", "cans"), 0, "Use 'could' with perception verbs in past context."),
                MCQ("______ you help me carry these heavy bags?", listOf("Could", "Might", "Should", "May"), 0, "Polite request is made with 'Could'.")
            )
        ),
        // 3. SHOULD
        GrammarRule(
            id = 3,
            title = "Modal Verb: Should",
            titleUrdu = "Should کا استعمال",
            category = "Modals",
            iconName = "explore",
            explanation = "Used to give advice, make recommendations, or express moral obligation and expectation.",
            explanationUrdu = "نصیحت کرنے، مشورہ دینے، یا اخلاقی فرض اور توقع ظاہر کرنے کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + should + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("You should eat more healthy food.", "آپ کو زیادہ صحت بخش کھانا کھانا چاہیے۔"),
                RuleExample("We should respect our elders.", "ہمیں اپنے بڑوں کا احترام کرنا چاہیے۔"),
                RuleExample("Should I call him right now?", "کیا مجھے اسے ابھی فون کرنا چاہیے؟"),
                RuleExample("You should not waste your time.", "آپ کو اپنا وقت ضائع نہیں کرنا چاہیے۔"),
                RuleExample("The bus should arrive in ten minutes.", "بس دس منٹ میں پہنچ جانی چاہیے۔ (توقع)")
            ),
            quiz = listOf(
                MCQ("You ______ visit the dentist regularly.", listOf("should", "must to", "could to", "ought"), 0, "Use 'should' for good advice."),
                MCQ("We ______ not make noise in the library.", listOf("should", "could", "would", "may"), 0, "Negative recommendation uses 'should not'."),
                MCQ("What ______ I wear to the wedding?", listOf("would", "could", "should", "may"), 2, "Use 'should' when asking for advice."),
                MCQ("You ______ apologize for your rude behavior.", listOf("should", "could to", "should to", "would to"), 0, "Use 'should' for moral obligation."),
                MCQ("The weather is clear; it ______ rain today.", listOf("should not", "could not", "must not to", "cannot to"), 0, "Use 'should not' for expectation based on clear evidence."),
                MCQ("He ______ study hard if he wants to pass.", listOf("should", "shall to", "must to", "could to"), 0, "Use 'should' for advice."),
                MCQ("Children ______ drink milk daily.", listOf("should", "must to", "could to", "shall to"), 0, "Recommendation for health uses 'should'."),
                MCQ("We ______ help the poor and needy.", listOf("should", "would to", "could to", "may to"), 0, "Use 'should' for moral advice."),
                MCQ("Where ______ we go for our summer holidays?", listOf("should", "will", "may", "would"), 0, "Asking for suggestions with 'should'."),
                MCQ("He ______ be at home by now.", listOf("should", "should to", "will to", "must to"), 0, "Use 'should' for probable expectation.")
            )
        ),
        // 4. WOULD
        GrammarRule(
            id = 4,
            title = "Modal Verb: Would",
            titleUrdu = "Would کا استعمال",
            category = "Modals",
            iconName = "phone_android",
            explanation = "Used for past habits, polite offers/invitations, expressing preference, or hypothetical/conditional situations.",
            explanationUrdu = "ماضی کی عادات، شائستہ پیشکش یا دعوت، پسندیدگی، یا فرضی حالات کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + would + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("Would you like a cup of tea?", "کیا آپ چائے کا ایک کپ پسند کریں گے؟"),
                RuleExample("I would rather stay home tonight.", "میں آج رات گھر پر رہنے کو ترجیح دوں گا۔"),
                RuleExample("If I were rich, I would travel the world.", "اگر میں امیر ہوتا، تو دنیا کا سفر کرتا۔"),
                RuleExample("My grandfather would tell us stories.", "میرے دادا ہمیں کہانیاں سنایا کرتے تھے (ماضی کی عادت)۔"),
                RuleExample("Would you mind helping me with this task?", "کیا آپ اس کام میں میری مدد کرنے کی زحمت کریں گے؟")
            ),
            quiz = listOf(
                MCQ("______ you like some coffee?", listOf("Would", "Should", "Could", "Will"), 0, "Polite offer starts with 'Would you like...'."),
                MCQ("If she studied, she ______ pass the exam.", listOf("will", "would", "shall", "can"), 1, "In second conditional, 'would' is used in the main clause."),
                MCQ("I ______ rather drink green tea than coffee.", listOf("should", "would", "could", "must"), 1, "The phrase 'would rather' indicates preference."),
                MCQ("Every Sunday, we ______ go fishing in the river.", listOf("will", "would", "should", "shall"), 1, "Use 'would' for repeated past habits."),
                MCQ("______ you mind opening the door?", listOf("Would", "Should", "Could", "Will"), 0, "The polite construction 'Would you mind + verb-ing' uses would."),
                MCQ("He promised that he ______ call me.", listOf("will", "would", "should", "can"), 1, "'would' is the past form of 'will' in reported speech."),
                MCQ("If I won the lottery, I ______ build a school.", listOf("will", "would", "shall", "must"), 1, "Hypothetical situation clause uses 'would'."),
                MCQ("She ______ not agree to their proposal.", listOf("would", "should to", "could to", "might to"), 0, "Use 'would' for past refusal."),
                MCQ("______ you prefer to sit inside or outside?", listOf("Would", "Should", "Could", "Might"), 0, "'Would you prefer' is standard polite choice expression."),
                MCQ("I ______ love to visit Pakistan someday.", listOf("should", "would", "could", "must"), 1, "'would love to' expresses strong desire.")
            )
        ),
        // 5. WILL
        GrammarRule(
            id = 5,
            title = "Modal Verb: Will",
            titleUrdu = "Will کا استعمال",
            category = "Modals",
            iconName = "favorite",
            explanation = "Used to express future events, rapid decisions, promises, predictions, or determination.",
            explanationUrdu = "مستقبل کے واقعات، فوری فیصلوں، وعدوں، پیشگوئیوں، یا پختہ ارادے کے اظہار کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + will + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("The train will arrive at 6:00 PM.", "ٹرین شام 6 بجے پہنچے گی۔"),
                RuleExample("I will help you with your project.", "میں آپ کے پروجیکٹ میں آپ کی مدد کروں گا (وعدہ)۔"),
                RuleExample("Wait, I will open the door for you.", "رکیے، میں آپ کے لیے دروازہ کھولتا ہوں (فوری فیصلہ)۔"),
                RuleExample("It will rain tomorrow, according to the news.", "خبروں کے مطابق کل بارش ہوگی (پیشگوئی)۔"),
                RuleExample("We will not tolerate this behavior.", "ہم اس رویے کو برداشت نہیں کریں گے (پختہ ارادہ)۔")
            ),
            quiz = listOf(
                MCQ("I ______ call you as soon as I arrive.", listOf("will", "would", "should", "shall to"), 0, "Use 'will' for a future promise/intention."),
                MCQ("Don't worry, they ______ not forget the keys.", listOf("will", "would", "should", "could"), 0, "Future negative prediction uses 'will not'."),
                MCQ("The phone is ringing. I ______ answer it.", listOf("will", "would", "should", "shall"), 0, "Immediate/rapid decision uses 'will'."),
                MCQ("In 2050, cars ______ fly in the sky.", listOf("will", "would", "should", "may"), 0, "Future prediction uses 'will'."),
                MCQ("______ you marry me?", listOf("Will", "Would", "Should", "Shall"), 0, "Formal proposal uses 'Will you...'."),
                MCQ("We ______ win this matches next week.", listOf("will", "should to", "must to", "will to"), 0, "Future statement uses 'will' + base verb."),
                MCQ("I ______ always love you.", listOf("will", "should", "would", "could"), 0, "Expressing a continuous promise uses 'will'."),
                MCQ("They ______ hold a press conference tomorrow.", listOf("will", "should to", "would to", "must to"), 0, "Scheduled future activity uses 'will'."),
                MCQ("If he works hard, he ______ pass.", listOf("will", "would", "should", "can to"), 0, "First conditional results clause uses 'will'."),
                MCQ("I think it ______ be hot today.", listOf("will", "would", "should", "could"), 0, "Opinion prediction uses 'will'.")
            )
        ),
        // 6. MAY
        GrammarRule(
            id = 6,
            title = "Modal Verb: May",
            titleUrdu = "May کا استعمال",
            category = "Modals",
            iconName = "info",
            explanation = "Used to ask for or give formal permission, express a strong possibility, or state wishes and prayers.",
            explanationUrdu = "رسمی اجازت مانگنے یا دینے، قوی امکان، یا دعا اور نیک خواہشات کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + may + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("May I come in, teacher?", "کیا میں اندر آ سکتا ہوں، استاد محترم؟"),
                RuleExample("You may leave now if you are finished.", "اگر آپ کا کام ختم ہو گیا ہے تو آپ اب جا سکتے ہیں۔"),
                RuleExample("It may rain tonight; the sky is cloudy.", "آج رات بارش ہو سکتی ہے؛ آسمان پر بادل چھائے ہوئے ہیں۔"),
                RuleExample("May Allah bless you with health and happiness!", "اللہ آپ کو صحت اور خوشیوں سے نوازے! (دعا)"),
                RuleExample("He may buy a new laptop next week.", "وہ اگلے ہفتے نیا لیپ ٹاپ خرید سکتا ہے (امکان)۔")
            ),
            quiz = listOf(
                MCQ("______ I ask a personal question, sir?", listOf("May", "Should", "Would", "Will"), 0, "'May' is the most polite/formal way to ask permission."),
                MCQ("The dark clouds show that it ______ rain today.", listOf("may", "should", "would", "must to"), 0, "Use 'may' for factual possibility."),
                MCQ("You ______ begin your writing test now.", listOf("may", "should to", "could to", "would to"), 0, "Formal granting of permission uses 'may'."),
                MCQ("______ you live a long and prosperous life!", listOf("May", "Should", "Would", "Could"), 0, "State wishes/prayers starting with 'May'."),
                MCQ("He is absent. He ______ be sick.", listOf("may", "should", "would to", "could to"), 0, "Use 'may' for present possibility."),
                MCQ("This path ______ be dangerous at night.", listOf("may", "should to", "would to", "must to"), 0, "Use 'may' to state a possibility."),
                MCQ("______ I park my bicycle here?", listOf("May", "Would", "Should", "Will"), 0, "Polite permission request uses 'May'."),
                MCQ("She ______ join us for dinner later.", listOf("may", "should to", "could to", "must to"), 0, "Uncertain but possible future event uses 'may'."),
                MCQ("They ______ not accept your excuse.", listOf("may", "should to", "would to", "could to"), 0, "Use 'may not' for negative possibility."),
                MCQ("May I go to the bathroom? Yes, you ______.", listOf("may", "can to", "should", "might"), 0, "Response to 'May I' is 'Yes, you may'.")
            )
        ),
        // 7. MIGHT
        GrammarRule(
            id = 7,
            title = "Modal Verb: Might",
            titleUrdu = "Might کا استعمال",
            category = "Modals",
            iconName = "star",
            explanation = "Used to express a weak, tentative, or very remote possibility in the present or future, or as a past form of 'may'.",
            explanationUrdu = "موجودہ یا مستقبل میں بہت کم، غیر یقینی یا بعید امکان کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + might + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("I might visit Lahore, but I am not sure.", "میں شاید لاہور جاؤں، لیکن میں یقین سے نہیں کہہ سکتا۔"),
                RuleExample("If you don't study, you might fail.", "اگر آپ پڑھائی نہیں کریں گے، تو شاید فیل ہو جائیں۔"),
                RuleExample("She might call you later tonight.", "وہ شاید آج رات گئے آپ کو کال کرے۔"),
                RuleExample("It might rain, but the sun is still shining.", "شاید بارش ہو، لیکن سورج اب بھی چمک رہا ہے۔"),
                RuleExample("We might buy a new car next year if budget allows.", "اگر بجٹ اجازت دے تو ہم اگلے سال شاید نئی گاڑی خریدیں۔")
            ),
            quiz = listOf(
                MCQ("We ______ go to the beach, but it depends on the weather.", listOf("might", "must", "should to", "would to"), 0, "Use 'might' for weak/uncertain possibility."),
                MCQ("If he ran faster, he ______ win.", listOf("might", "can", "should to", "must to"), 0, "'might' is used in remote conditionals."),
                MCQ("Take an umbrella. It ______ rain later.", listOf("might", "must to", "should to", "would to"), 0, "Use 'might' for warning about a weak possibility."),
                MCQ("I ______ not be able to attend the class tomorrow.", listOf("might", "must to", "should to", "could to"), 0, "Use 'might not' for uncertain negative future."),
                MCQ("She ______ be at home, but I haven't checked.", listOf("might", "should to", "must to", "will to"), 0, "Use 'might' for low certainty."),
                MCQ("They ______ have arrived by now, but the train was delayed.", listOf("might", "should to", "must to", "would to"), 0, "Speculative past uses 'might'."),
                MCQ("Ask him. He ______ know the answer.", listOf("might", "must to", "should to", "would to"), 0, "Use 'might' for tentative possibility."),
                MCQ("It ______ be cold tonight, so wear a jacket.", listOf("might", "should to", "must to", "would to"), 0, "Suggested possibility uses 'might'."),
                MCQ("He ______ join the army after college.", listOf("might", "must to", "should to", "could to"), 0, "Weak prediction uses 'might'."),
                MCQ("If I have time, I ______ call him.", listOf("might", "must", "should to", "would to"), 0, "Conditional possibility uses 'might'.")
            )
        ),
        // 8. MUST
        GrammarRule(
            id = 8,
            title = "Modal Verb: Must",
            titleUrdu = "Must کا استعمال",
            category = "Modals",
            iconName = "warning",
            explanation = "Used to express strong obligation, absolute necessity, duty, or logical certainty based on strong evidence.",
            explanationUrdu = "شدید ضرورت، لازمی فرض، اخلاقی ذمہ داری، یا مضبوط ثبوت پر مبنی یقینی بات کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔",
            formula = "Subject + must + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("You must wear a helmet while riding a bike.", "موٹر سائیکل چلاتے وقت آپ کو ہیلمٹ لازمی پہننا چاہیے۔"),
                RuleExample("We must respect the laws of our country.", "ہمیں اپنے ملک کے قوانین کا لازمی احترام کرنا چاہیے۔"),
                RuleExample("You must not smoke inside the hospital.", "آپ کو ہسپتال کے اندر سگریٹ نوشی بالکل نہیں کرنی چاہیے۔"),
                RuleExample("She has worked all day; she must be exhausted.", "اس نے سارا دن کام کیا ہے؛ وہ یقیناً تھک گئی ہوگی۔"),
                RuleExample("I must finish this assignment tonight.", "مجھے آج رات یہ اسائنمنٹ ہر حال میں مکمل کرنی ہے۔")
            ),
            quiz = listOf(
                MCQ("You ______ stop at the red light.", listOf("must", "should to", "could to", "may"), 0, "Law and absolute obligation require 'must'."),
                MCQ("He has three luxury cars. He ______ be rich.", listOf("must", "should", "would", "might to"), 0, "Logical deduction based on strong evidence uses 'must'."),
                MCQ("We ______ not feed the animals in the zoo.", listOf("must", "should to", "could to", "might to"), 0, "Strict prohibition uses 'must not'."),
                MCQ("I ______ submit the tax return today or face a fine.", listOf("must", "should to", "could to", "may to"), 0, "Absolute legal necessity uses 'must'."),
                MCQ("You ______ tell anyone this secret.", listOf("must not", "should not to", "cannot to", "dont must"), 0, "Strong prohibition uses 'must not'."),
                MCQ("Every citizen ______ pay taxes.", listOf("must", "should to", "could to", "may to"), 0, "Strict duty or obligation uses 'must'."),
                MCQ("The baby is crying; she ______ be hungry.", listOf("must", "should", "would", "could"), 0, "Highly probable logical deduction uses 'must'."),
                MCQ("To apply for this job, you ______ have a degree.", listOf("must", "should to", "could to", "might"), 0, "Prerequisite/requirement uses 'must'."),
                MCQ("We ______ protect our environment.", listOf("must", "should to", "could to", "may to"), 0, "Strong moral necessity uses 'must'."),
                MCQ("You ______ see a doctor immediately. You look very ill.", listOf("must", "should to", "could to", "would to"), 0, "Strongly urged recommendation uses 'must'.")
            )
        ),
        // 9. PRESENT SIMPLE
        GrammarRule(
            id = 9,
            title = "Present Simple Tense",
            titleUrdu = "فعل حال سادہ",
            category = "Tenses",
            iconName = "school",
            explanation = "Used to describe routines, habits, general facts, universal truths, and permanent states.",
            explanationUrdu = "روزمرہ عادات، معمولات، عام سچائیوں اور مستقل حالات کو ظاہر کرنے کے لیے استعمال ہوتا ہے۔ جملے کے آخر میں 'تا ہے'، 'تی ہے'، 'تے ہیں' آتا ہے۔",
            formula = "Subject + Verb (1st form) (+ s/es for He/She/It/Singular) + Object",
            examples = listOf(
                RuleExample("He drinks water every morning.", "وہ ہر صبح پانی پیتا ہے۔"),
                RuleExample("We live in Islamabad.", "ہم اسلام آباد میں رہتے ہیں۔"),
                RuleExample("The sun rises in the east.", "سورج مشرق سے نکلتا ہے۔"),
                RuleExample("Does she teach English?", "کیا وہ انگریزی پڑھاتی ہے؟"),
                RuleExample("They do not play cricket on Monday.", "وہ پیر کے دن کرکٹ نہیں کھیلتے ہیں۔")
            ),
            quiz = listOf(
                MCQ("Ali ______ to school every day.", listOf("goes", "go", "going", "gone"), 0, "Singular subject (Ali) takes verb+s/es."),
                MCQ("We ______ in Lahore.", listOf("live", "lives", "living", "lived"), 0, "Plural subject (We) takes base form of verb."),
                MCQ("She ______ not like fast food.", listOf("does", "do", "is", "has"), 0, "Singular negative helper is 'does'."),
                MCQ("______ they speak English?", listOf("Do", "Does", "Is", "Are"), 0, "Plural question helper is 'Do'."),
                MCQ("The Earth ______ around the Sun.", listOf("revolves", "revolve", "revolving", "revolved"), 0, "Universal truth with singular subject takes verb+s/es."),
                MCQ("He ______ study hard for exams.", listOf("does not", "do not", "is not", "not"), 0, "Singular negative is 'does not'."),
                MCQ("Water ______ at 100 degrees Celsius.", listOf("boils", "boil", "boiling", "boiled"), 0, "Scientific fact is in Present Simple (singular subject takes 'boils')."),
                MCQ("I ______ my teeth twice a day.", listOf("brush", "brushes", "brushing", "brushed"), 0, "First person pronoun takes base verb."),
                MCQ("What time ______ the bank close?", listOf("does", "do", "is", "has"), 0, "'the bank' is singular, so use 'does'."),
                MCQ("They ______ television in the evening.", listOf("watch", "watches", "watching", "watched"), 0, "Plural subject takes base verb.")
            )
        ),
        // 10. PRESENT CONTINUOUS
        GrammarRule(
            id = 10,
            title = "Present Continuous Tense",
            titleUrdu = "فعل حال جاری",
            category = "Tenses",
            iconName = "emoji_emotions",
            explanation = "Used for actions happening right now, at the moment of speaking, or temporary situations.",
            explanationUrdu = "ایسے کاموں کے لیے جو اس وقت جاری ہوں جب بات کی جا رہی ہو، یا عارضی حالات کے لیے۔ جملے کے آخر میں 'رہا ہے'، 'رہی ہے'، 'رہے ہیں' آتا ہے۔",
            formula = "Subject + is/am/are + Verb-ing + Object",
            examples = listOf(
                RuleExample("She is writing a letter now.", "وہ ابھی ایک خط لکھ رہی ہے۔"),
                RuleExample("I am learning English grammar.", "میں انگریزی گرامر سیکھ رہا ہوں جس وقت بات ہو رہی ہے۔"),
                RuleExample("They are playing football outside.", "وہ باہر فٹ بال کھیل رہے ہیں۔"),
                RuleExample("Are you watching a movie?", "کیا آپ فلم دیکھ رہے ہیں؟"),
                RuleExample("We are not going to Lahore today.", "ہم آج لاہور نہیں جا رہے ہیں۔")
            ),
            quiz = listOf(
                MCQ("Listen! The birds ______ in the garden.", listOf("are singing", "is singing", "sing", "singing"), 0, "'The birds' is plural, takes 'are' + verb-ing."),
                MCQ("I ______ my homework at the moment.", listOf("am doing", "is doing", "are doing", "do"), 0, "Subject 'I' takes 'am doing'."),
                MCQ("Why ______ you crying?", listOf("are", "is", "am", "do"), 0, "Subject 'you' takes helping verb 'are'."),
                MCQ("He ______ not working today.", listOf("is", "am", "are", "does"), 0, "Subject 'He' takes helping verb 'is'."),
                MCQ("Look! It ______ to rain.", listOf("is starting", "are starting", "starts", "starting"), 0, "Subject 'It' takes 'is starting'."),
                MCQ("We ______ a delicious dinner right now.", listOf("are eating", "is eating", "am eating", "eat"), 0, "Subject 'We' takes 'are' + verb-ing."),
                MCQ("______ she studying for her final exams?", listOf("Is", "Are", "Am", "Does"), 0, "Subject 'she' takes helping verb 'Is'."),
                MCQ("They ______ a new house in Islamabad this month.", listOf("are building", "is building", "builds", "building"), 0, "Temporary ongoing situation takes present continuous."),
                MCQ("I ______ not feeling well today.", listOf("am", "is", "are", "do"), 0, "Subject 'I' takes 'am'."),
                MCQ("Who ______ standing at the door?", listOf("is", "are", "am", "does"), 0, "Who acts as singular here, taking 'is'.")
            )
        ),
        // 11. PRESENT PERFECT
        GrammarRule(
            id = 11,
            title = "Present Perfect Tense",
            titleUrdu = "فعل حال مکمل",
            category = "Tenses",
            iconName = "checkroom",
            explanation = "Used to describe completed actions with an influence on the present, or actions completed very recently.",
            explanationUrdu = "ایسے کاموں کے لیے جو حال ہی میں مکمل ہوئے ہوں اور ان کا اثر اب بھی موجود ہو۔ جملے کے آخر میں 'چکا ہے'، 'چکی ہے'، 'لیا ہے'، 'دی ہے' آتا ہے۔",
            formula = "Subject + has/have + Verb (3rd form) + Object",
            examples = listOf(
                RuleExample("He has finished his homework.", "اس نے اپنا ہوم ورک مکمل کر لیا ہے۔"),
                RuleExample("I have visited Murree twice.", "میں دو بار مری کی سیر کر چکا ہوں۔"),
                RuleExample("They have lost their car keys.", "وہ اپنے کار کی چابیاں کھو چکے ہیں۔"),
                RuleExample("Have you ever eaten biryani?", "کیا آپ نے کبھی بریانی کھائی ہے؟"),
                RuleExample("She has not written the essay yet.", "اس نے ابھی تک مضمون نہیں لکھا ہے۔")
            ),
            quiz = listOf(
                MCQ("She ______ lived in Karachi for five years.", listOf("has", "have", "is", "had"), 0, "Singular subject 'She' takes 'has'."),
                MCQ("I ______ already read this amazing book.", listOf("have", "has", "am", "had"), 0, "Subject 'I' takes 'have'."),
                MCQ("They ______ not arrived yet.", listOf("have", "has", "are", "were"), 0, "Plural subject 'They' takes 'have'."),
                MCQ("______ you ever been to Islamabad?", listOf("Have", "Has", "Are", "Did"), 0, "Subject 'you' takes helping verb 'Have'."),
                MCQ("He ______ lost his mobile phone.", listOf("has", "have", "is", "was"), 0, "Subject 'He' takes 'has'."),
                MCQ("We ______ not seen him since Monday.", listOf("have", "has", "did", "had"), 0, "Subject 'We' takes 'have'."),
                MCQ("The teacher ______ already graded the papers.", listOf("has", "have", "is", "was"), 0, "Singular 'The teacher' takes 'has'."),
                MCQ("______ she finished her lunch?", listOf("Has", "Have", "Is", "Does"), 0, "Singular 'she' takes 'Has'."),
                MCQ("I ______ my car keys. I can't open the door.", listOf("have lost", "has lost", "lost", "lose"), 0, "Use present perfect to connect past action to present state."),
                MCQ("They ______ a new car.", listOf("have bought", "has bought", "bought", "buy"), 0, "Plural 'They' takes 'have bought'.")
            )
        ),
        // 12. PAST SIMPLE
        GrammarRule(
            id = 12,
            title = "Past Simple Tense",
            titleUrdu = "فعل ماضی سادہ",
            category = "Tenses",
            iconName = "explore",
            explanation = "Used to describe completed actions that took place at a specific time in the past.",
            explanationUrdu = "ماضی میں کسی مخصوص وقت پر مکمل ہونے والے کاموں کے لیے۔ جملے کے آخر میں 'الف'، 'ی'، 'ے'، یا 'تھا' آتا ہے۔",
            formula = "Subject + Verb (2nd form) + Object | Neg: did not + Verb (1st form)",
            examples = listOf(
                RuleExample("We visited Lahore last year.", "ہم نے پچھلے سال لاہور کی سیر کی۔"),
                RuleExample("He bought a new laptop yesterday.", "اس نے کل ایک نیا لیپ ٹاپ خریدا۔"),
                RuleExample("I did not watch the match last night.", "میں نے کل رات میچ نہیں دیکھا۔"),
                RuleExample("Did you receive my email?", "کیا آپ کو میرا ای میل موصول ہوا؟"),
                RuleExample("She told me a beautiful story.", "اس نے مجھے ایک خوبصورت کہانی سنائی۔")
            ),
            quiz = listOf(
                MCQ("They ______ to Murree last week.", listOf("went", "go", "gone", "going"), 0, "Use 2nd form of verb (went) for past simple."),
                MCQ("I ______ not see him at the party yesterday.", listOf("did", "do", "does", "was"), 0, "Past simple negative helping verb is 'did'."),
                MCQ("Did you ______ your homework?", listOf("do", "did", "done", "doing"), 0, "After 'did', use the 1st form of the verb (do)."),
                MCQ("She ______ a beautiful song yesterday.", listOf("sang", "sing", "sung", "singing"), 0, "Use 2nd form of verb (sang)."),
                MCQ("When ______ they arrive?", listOf("did", "do", "does", "have"), 0, "Past simple question helper is 'did'."),
                MCQ("My father ______ a new car last month.", listOf("bought", "buy", "buys", "buying"), 0, "Use 2nd form of verb (bought)."),
                MCQ("We ______ not watch TV last night.", listOf("did", "do", "was", "were"), 0, "Negative past uses 'did not' + 1st form."),
                MCQ("I ______ a letter to my friend last Sunday.", listOf("wrote", "write", "written", "writing"), 0, "Use 2nd form of verb (wrote)."),
                MCQ("______ you sleep well last night?", listOf("Did", "Do", "Were", "Had"), 0, "Past simple question starts with 'Did'."),
                MCQ("He ______ the exam easily.", listOf("passed", "pass", "passing", "passes"), 0, "Use 2nd form of verb (passed).")
            )
        ),
        // 13. PAST CONTINUOUS
        GrammarRule(
            id = 13,
            title = "Past Continuous Tense",
            titleUrdu = "فعل ماضی جاری",
            category = "Tenses",
            iconName = "phone_android",
            explanation = "Used for actions that were ongoing at a specific time in the past or interrupted by another past action.",
            explanationUrdu = "ماضی میں کسی مخصوص وقت پر جاری رہنے والے کاموں کے لیے۔ جملے کے آخر میں 'رہا تھا'، 'رہی تھی'، 'رہے تھے' آتا ہے۔",
            formula = "Subject + was/were + Verb-ing + Object",
            examples = listOf(
                RuleExample("I was reading a book when she arrived.", "جب وہ آئی تو میں کتاب پڑھ رہا تھا۔"),
                RuleExample("They were playing cricket all afternoon.", "وہ پوری دوپہر کرکٹ کھیل رہے تھے۔"),
                RuleExample("She was not sleeping when the phone rang.", "جب فون بجا تو وہ سو نہیں رہی تھی۔"),
                RuleExample("What were you doing at 8 PM yesterday?", "کل رات 8 بجے آپ کیا کر رہے تھے؟"),
                RuleExample("He was driving a car while listening to music.", "وہ موسیقی سنتے ہوئے گاڑی چلا رہا تھا۔")
            ),
            quiz = listOf(
                MCQ("He ______ sleeping when I called him.", listOf("was", "were", "is", "did"), 0, "Singular 'He' takes 'was' + verb-ing."),
                MCQ("They ______ playing football in the rain.", listOf("were", "was", "are", "did"), 0, "Plural 'They' takes 'were' + verb-ing."),
                MCQ("What ______ you doing yesterday evening?", listOf("were", "was", "did", "are"), 0, "Subject 'you' takes helping verb 'were'."),
                MCQ("She ______ not writing anything during class.", listOf("was", "were", "is", "did"), 0, "Singular 'She' takes 'was'."),
                MCQ("While I ______ cooking, the lights went out.", listOf("was", "were", "am", "did"), 0, "Subject 'I' takes 'was' in past continuous."),
                MCQ("We ______ studying together for the test.", listOf("were", "was", "are", "had"), 0, "Subject 'We' takes 'were'."),
                MCQ("It ______ raining when we stepped outside.", listOf("was", "were", "is", "did"), 0, "Subject 'It' takes 'was'."),
                MCQ("The children ______ making noise in the room.", listOf("were", "was", "are", "did"), 0, "Plural 'The children' takes 'were'."),
                MCQ("______ she listening to the news on the radio?", listOf("Was", "Were", "Did", "Is"), 0, "Singular 'she' takes 'Was'."),
                MCQ("I ______ dreaming about flying last night.", listOf("was", "were", "am", "did"), 0, "Subject 'I' takes 'was'.")
            )
        ),
        // 14. FUTURE SIMPLE
        GrammarRule(
            id = 14,
            title = "Future Simple Tense",
            titleUrdu = "فعل مستقبل سادہ",
            category = "Tenses",
            iconName = "favorite",
            explanation = "Used for actions that will take place in the future, predictions, or promises.",
            explanationUrdu = "مستقبل میں ہونے والے کسی بھی کام کو ظاہر کرنے کے لیے۔ جملے کے آخر میں 'گا'، 'گی'، 'گے' آتا ہے۔",
            formula = "Subject + will + Verb (1st form) + Object",
            examples = listOf(
                RuleExample("We will attend the seminar tomorrow.", "ہم کل سیمینار میں شرکت کریں گے۔"),
                RuleExample("I will call you tonight.", "میں آپ کو آج رات فون کروں گا۔"),
                RuleExample("She will not go to the market today.", "وہ آج بازار نہیں جائے گی۔"),
                RuleExample("Will they win the cricket match?", "کیا وہ کرکٹ میچ جیتیں گے؟"),
                RuleExample("It will be a sunny day tomorrow.", "کل ایک دھوپ والا دن ہوگا۔")
            ),
            quiz = listOf(
                MCQ("I ______ help you pack your bags.", listOf("will", "would", "should", "shall to"), 0, "Use 'will' for voluntary future offer."),
                MCQ("They ______ visit Pakistan next month.", listOf("will", "would", "shall to", "must to"), 0, "Use 'will' for planned future event."),
                MCQ("She ______ not agree to this plan.", listOf("will", "would", "should", "does"), 0, "Use 'will not' for negative future prediction."),
                MCQ("______ you attend the meeting tomorrow?", listOf("Will", "Would", "Should", "Do"), 0, "Use 'Will' for future event question."),
                MCQ("The weather reporter says it ______ rain tomorrow.", listOf("will", "would", "should", "could"), 0, "Prediction of future weather uses 'will'."),
                MCQ("We ______ finish this project on time.", listOf("will", "should to", "could to", "will to"), 0, "Expression of determination uses 'will'."),
                MCQ("What ______ you do after graduation?", listOf("will", "would", "do", "should"), 0, "Asking about future plans uses 'will'."),
                MCQ("He ______ buy a new house next year.", listOf("will", "would to", "should to", "could to"), 0, "Simple future statement uses 'will'."),
                MCQ("I promise I ______ not tell anyone.", listOf("will", "would", "should", "do"), 0, "Promise uses 'will not' / 'won't'."),
                MCQ("The class ______ start at 9:00 AM tomorrow.", listOf("will", "would", "is", "should to"), 0, "Scheduled future event uses 'will'.")
            )
        ),
        // 15. SUBJECT-VERB AGREEMENT
        GrammarRule(
            id = 15,
            title = "Subject-Verb Agreement",
            titleUrdu = "فاعل اور فعل کی مطابقت",
            category = "Grammar",
            iconName = "info",
            explanation = "Singular subjects require singular verbs, and plural subjects require plural verbs.",
            explanationUrdu = "واحد فاعل کے ساتھ واحد فعل اور جمع فاعل کے ساتھ جمع فعل آتا ہے۔ ہی، شی، اٹ کے ساتھ فعل کے آخر میں 's' یا 'es' لگایا جاتا ہے۔",
            formula = "Singular Subj -> Verb+s | Plural Subj -> Base Verb",
            examples = listOf(
                RuleExample("The boy walks to school daily.", "لڑکا روزانہ پیدل اسکول جاتا ہے۔ (واحد فاعل)"),
                RuleExample("The boys walk to school daily.", "لڑکے روزانہ پیدل اسکول جاتے ہیں۔ (جمع فاعل)"),
                RuleExample("He plays football on Sunday.", "وہ اتوار کو فٹ بال کھیلتا ہے۔"),
                RuleExample("They play football on Sunday.", "وہ اتوار کو فٹ بال کھیلتے ہیں۔"),
                RuleExample("Everyone loves a good story.", "ہر کوئی ایک اچھی کہانی سے محبت کرتا ہے۔ (Everyone واحد ہے)")
            ),
            quiz = listOf(
                MCQ("The dog ______ loudly at night.", listOf("barks", "bark", "barking", "barked"), 0, "Singular 'The dog' takes verb+s (barks)."),
                MCQ("My friends ______ planning a surprise party.", listOf("are", "is", "am", "was"), 0, "Plural 'My friends' takes plural verb 'are'."),
                MCQ("Either Ali or Ahmad ______ coming today.", listOf("is", "are", "am", "were"), 0, "Subjects connected by 'either... or' take verb according to the nearest singular subject (is)."),
                MCQ("Every student ______ to pass the exam.", listOf("wants", "want", "wanting", "wants to"), 0, "Indefinite pronouns like 'Every' are singular, takes verb+s (wants)."),
                MCQ("A group of students ______ waiting outside.", listOf("is", "are", "were", "am"), 0, "'A group' is a collective singular noun, taking 'is'."),
                MCQ("The news ______ very shocking today.", listOf("is", "are", "were", "am"), 0, "'News' is uncountable and takes a singular verb (is)."),
                MCQ("Ten dollars ______ too much for this coffee.", listOf("is", "are", "were", "am"), 0, "Moneys, distances, and times take singular verbs when considered as a unit (is)."),
                MCQ("They ______ delicious food at this restaurant.", listOf("serve", "serves", "serving", "served"), 0, "Plural subject 'They' takes base verb (serve)."),
                MCQ("The book, along with its notes, ______ on the table.", listOf("is", "are", "were", "am"), 0, "Parenthetical expressions do not change the singular subject 'The book' (is)."),
                MCQ("Both of the sisters ______ very talented.", listOf("are", "is", "was", "am"), 0, "'Both' is always plural, takes 'are'.")
            )
        ),
        // 16. USE OF SINCE & FOR
        GrammarRule(
            id = 16,
            title = "Use of Since and For",
            titleUrdu = "Since اور For کا استعمال",
            category = "Grammar",
            iconName = "star",
            explanation = "Use 'since' for a specific starting point in time. Use 'for' for a length or duration of time.",
            explanationUrdu = "وقت کے متعین نقطہ آغاز کے لیے 'since' اور وقت کے دورانیہ یا وقفے کے لیے 'for' کا استعمال کیا جاتا ہے۔",
            formula = "Since + Point of Time (since 1999) | For + Period of Time (for 3 years)",
            examples = listOf(
                RuleExample("She has been sleeping since 3:00 PM.", "وہ سہ پہر 3:00 بجے سے سو رہی ہے۔"),
                RuleExample("They have lived in Lahore for five years.", "وہ پانچ سال سے لاہور میں رہ رہے ہیں۔"),
                RuleExample("I have not seen him since Monday.", "میں نے اسے پیر کے دن سے نہیں دیکھا ہے۔"),
                RuleExample("He has been playing games for two hours.", "وہ دو گھنٹے سے گیمز کھیل رہا ہے۔"),
                RuleExample("We have been friends since our childhood.", "ہم اپنے بچپن سے ہی دوست ہیں۔")
            ),
            quiz = listOf(
                MCQ("They have been waiting ______ two hours.", listOf("for", "since", "from", "during"), 0, "'two hours' is a duration, uses 'for'."),
                MCQ("I have lived here ______ 2015.", listOf("since", "for", "from", "in"), 0, "'2015' is a specific point in time, uses 'since'."),
                MCQ("She hasn't eaten anything ______ morning.", listOf("since", "for", "from", "by"), 0, "'morning' is a starting point, uses 'since'."),
                MCQ("We have been studying English ______ six months.", listOf("for", "since", "from", "during"), 0, "'six months' is a period of time, uses 'for'."),
                MCQ("He has been sick ______ last week.", listOf("since", "for", "from", "on"), 0, "'last week' is a point in time, uses 'since'."),
                MCQ("They have lived in Pakistan ______ many years.", listOf("for", "since", "from", "during"), 0, "'many years' is a duration, uses 'for'."),
                MCQ("I have not visited the museum ______ my childhood.", listOf("since", "for", "from", "during"), 0, "'childhood' is a starting point, uses 'since'."),
                MCQ("He has been working on the computer ______ three hours.", listOf("for", "since", "from", "by"), 0, "'three hours' is a duration, uses 'for'."),
                MCQ("The classes have been suspended ______ yesterday.", listOf("since", "for", "from", "during"), 0, "'yesterday' is a point in time, uses 'since'."),
                MCQ("She has been driving ______ 45 minutes.", listOf("for", "since", "from", "during"), 0, "'45 minutes' is a duration, uses 'for'.")
            )
        ),
        // 17. ACTIVE VS PASSIVE VOICE
        GrammarRule(
            id = 17,
            title = "Active vs Passive Voice",
            titleUrdu = "معروف اور مجہول آواز",
            category = "Grammar",
            iconName = "warning",
            explanation = "In active voice, the subject performs the action. In passive voice, the subject receives the action.",
            explanationUrdu = "معروف جملے میں فاعل کام کرتا ہے، جبکہ مجہول میں مفعول پر کام واقع ہوتا ہے اور فاعل ثانوی ہو جاتا ہے۔",
            formula = "Active: S+V+O | Passive: O + helping verb + V(3rd form) + by + S",
            examples = listOf(
                RuleExample("Ali wrote this letter.", "علی نے یہ خط لکھا (Active)۔"),
                RuleExample("This letter was written by Ali.", "یہ خط علی کے ذریعے لکھا گیا (Passive)۔"),
                RuleExample("The chef prepares delicious food.", "شیف لذیذ کھانا تیار کرتا ہے (Active)۔"),
                RuleExample("Delicious food is prepared by the chef.", "لذیذ کھانا شیف کے ذریعے تیار کیا جاتا ہے (Passive)۔"),
                RuleExample("They will win the match.", "وہ میچ جیت جائیں گے (Active)۔")
            ),
            quiz = listOf(
                MCQ("Identify: 'The letter was written by Ahmad.'", listOf("Passive Voice", "Active Voice", "Both", "None"), 0, "The subject is acted upon, making it passive."),
                MCQ("Change to Passive: 'She cooks food.'", listOf("Food is cooked by her.", "Food was cooked by her.", "Food cooks by her.", "Food is cooking by her."), 0, "Present simple active becomes 'is + 3rd form' in passive."),
                MCQ("Change to Active: 'The mouse was killed by the cat.'", listOf("The cat killed the mouse.", "The cat kills the mouse.", "The cat was killing the mouse.", "The cat has killed the mouse."), 0, "Past simple passive became simple past active verb."),
                MCQ("In Passive voice, we always use the ______ form of the verb.", listOf("3rd form", "1st form", "2nd form", "ing form"), 0, "Passive voice always uses the 3rd form (past participle) of the verb."),
                MCQ("Identify: 'The teacher praised the student.'", listOf("Active Voice", "Passive Voice", "Both", "None"), 0, "Subject 'teacher' performed the action of praising."),
                MCQ("Passive of 'He is reading a book.'", listOf("A book is being read by him.", "A book was read by him.", "A book is read by him.", "A book has been read by him."), 0, "Present continuous active becomes 'is being + 3rd form' in passive."),
                MCQ("Passive of 'I will buy a car.'", listOf("A car will be bought by me.", "A car would be bought by me.", "A car is bought by me.", "A car will bought by me."), 0, "Future simple active becomes 'will be + 3rd form' in passive."),
                MCQ("Passive of 'They have built a house.'", listOf("A house has been built by them.", "A house have been built by them.", "A house was built by them.", "A house has built by them."), 0, "Present perfect active becomes 'has/have been + 3rd form' in passive."),
                MCQ("Identify: 'My car was stolen yesterday.'", listOf("Passive Voice", "Active Voice", "Both", "None"), 0, "The car received the action of being stolen, so it is passive."),
                MCQ("Passive of 'Did he invite you?'", listOf("Were you invited by him?", "Was you invited by him?", "Are you invited by him?", "Did you invited by him?"), 0, "Past simple question passive starts with 'Was/Were' + subject + 3rd form.")
            )
        ),
        // 18. CONDITIONAL SENTENCES
        GrammarRule(
            id = 18,
            title = "Conditional Sentences (Type 1)",
            titleUrdu = "شرطیہ جملے (قسم اول)",
            category = "Grammar",
            iconName = "school",
            explanation = "Expresses a real, possible condition and its highly likely future outcome.",
            explanationUrdu = "مستقبل کے ممکنہ حالات اور ان کی شرط کو ظاہر کرتے ہیں۔ شرط والا حصہ حال سادہ میں اور جواب مستقبل سادہ میں ہوتا ہے۔",
            formula = "If + Present Simple, will + Verb (1st form)",
            examples = listOf(
                RuleExample("If you study hard, you will pass the exam.", "اگر آپ سخت محنت کریں گے، تو پاس ہو جائیں گے۔"),
                RuleExample("If it rains, we will stay at home.", "اگر بارش ہوئی، تو ہم گھر پر رہیں گے۔"),
                RuleExample("If he calls me, I will talk to him.", "اگر اس نے مجھے فون کیا، تو میں اس سے بات کروں گا۔"),
                RuleExample("If we leave early, we will catch the train.", "اگر ہم جلدی روانہ ہوئے، تو ٹرین پکڑ لیں گے۔"),
                RuleExample("She will be late if she doesn't hurry up.", "اگر اس نے جلدی نہ کی، تو وہ لیٹ ہو جائے گی۔")
            ),
            quiz = listOf(
                MCQ("If you ______ hard, you will pass.", listOf("study", "studied", "will study", "studies"), 0, "Conditional 'if' clause uses present simple (study)."),
                MCQ("If it rains tomorrow, we ______ the picnic.", listOf("will cancel", "would cancel", "canceled", "cancel"), 0, "Main clause of Type 1 conditional uses 'will' + base verb."),
                MCQ("She ______ angry if you are late.", listOf("will be", "would be", "is", "was"), 0, "Result clause uses future simple (will be)."),
                MCQ("If he ______ free, he will join us.", listOf("is", "will be", "was", "were"), 0, "Singular conditional uses present simple helping verb (is)."),
                MCQ("If they don't invite him, he ______ not come.", listOf("will", "would", "does", "did"), 0, "Future negative outcome uses 'will'."),
                MCQ("What will you do if you ______ the lottery?", listOf("win", "won", "will win", "winning"), 0, "'if' clause in Type 1 uses Present Simple (win)."),
                MCQ("If she ______ up early, she will catch the bus.", listOf("gets", "get", "will get", "got"), 0, "Singular third-person subject takes present simple (gets)."),
                MCQ("If you eat too much, you ______ sick.", listOf("will feel", "would feel", "felt", "feel"), 0, "Future simple result uses 'will feel'."),
                MCQ("They will arrive on time if they ______ a taxi.", listOf("take", "took", "will take", "takes"), 0, "'if' clause uses present simple (take)."),
                MCQ("If I ______ time, I will finish the report.", listOf("have", "has", "will have", "had"), 0, "First person present tense uses 'have'.")
            )
        ),
        // 19. INFINITIVES & GERUNDS
        GrammarRule(
            id = 19,
            title = "Infinitives and Gerunds",
            titleUrdu = "انفینیٹیو اور جیرنڈ کا فرق",
            category = "Grammar",
            iconName = "emoji_emotions",
            explanation = "An infinitive is 'to + verb' (to run). A gerund is a verb ending in 'ing' acting as a noun (running).",
            explanationUrdu = "ورب کی پہلی فارم سے پہلے 'to' لگانا انفینیٹیو ہے، اور آئی-این-جی لگا کر اسم (Noun) بنانا جیرنڈ کہلاتا ہے۔",
            formula = "Infinitive: to + Verb (1st form) | Gerund: Verb + ing",
            examples = listOf(
                RuleExample("I want to learn English.", "میں انگریزی سیکھنا چاہتا ہوں (Infinitive)۔"),
                RuleExample("Swimming is an excellent exercise.", "تیرنا ایک بہترین ورزش ہے (Gerund)۔"),
                RuleExample("He decided to buy a new laptop.", "اس نے ایک نیا لیپ ٹاپ خریدنے کا فیصلہ کیا۔"),
                RuleExample("They enjoy playing football on Sundays.", "وہ اتوار کو فٹ بال کھیلنے سے لطف اندوز ہوتے ہیں۔"),
                RuleExample("It is very healthy to walk daily.", "روزانہ چلنا صحت کے لیے بہت اچھا ہے۔")
            ),
            quiz = listOf(
                MCQ("I hope ______ you soon.", listOf("to see", "seeing", "see", "saw"), 0, "The verb 'hope' is followed by an infinitive (to see)."),
                MCQ("She enjoys ______ books in her free time.", listOf("reading", "to read", "read", "reads"), 0, "The verb 'enjoy' is followed by a gerund (reading)."),
                MCQ("They decided ______ a new house.", listOf("to buy", "buying", "buy", "bought"), 0, "The verb 'decided' is followed by an infinitive (to buy)."),
                MCQ("______ is forbidden in this hospital.", listOf("Smoking", "To smoke", "Smoke", "Smokes"), 0, "Using a gerund (Smoking) as the subject of the sentence."),
                MCQ("We offered ______ them with their luggage.", listOf("to help", "helping", "help", "helped"), 0, "The verb 'offer' is followed by an infinitive (to help)."),
                MCQ("He is good at ______ the guitar.", listOf("playing", "to play", "play", "plays"), 0, "Prepositions like 'at' are followed by a gerund (playing)."),
                MCQ("I avoided ______ his questions.", listOf("answering", "to answer", "answer", "answers"), 0, "The verb 'avoid' is followed by a gerund (answering)."),
                MCQ("It is important ______ daily.", listOf("to exercise", "exercising", "exercise", "exercised"), 0, "'It is adjective + infinitive' structure (to exercise)."),
                MCQ("She is planning ______ to Islamabad.", listOf("to travel", "traveling", "travel", "traveled"), 0, "The verb 'plan' is followed by an infinitive (to travel)."),
                MCQ("Thank you for ______ me.", listOf("helping", "to help", "help", "helped"), 0, "Preposition 'for' is followed by a gerund (helping).")
            )
        ),
        // 20. ARTICLES: A, AN, THE
        GrammarRule(
            id = 20,
            title = "Articles: A, An, The",
            titleUrdu = "حروف تعریف و تنکیر کا استعمال",
            category = "Grammar",
            iconName = "checkroom",
            explanation = "Use 'a' before consonant sounds, 'an' before vowel sounds, and 'the' for specific, unique, or previously mentioned nouns.",
            explanationUrdu = "عام اسم سے پہلے a/an اور خاص، منفرد یا پہلے سے ذکر کردہ اسم کے لیے the کا استعمال کریں۔",
            formula = "A + Consonant | An + Vowel Sound | The + Specific/Unique",
            examples = listOf(
                RuleExample("I saw an elephant in the zoo.", "میں نے چڑیا گھر میں ایک ہاتھی دیکھا۔"),
                RuleExample("She reads a book every night.", "وہ ہر رات ایک کتاب پڑھتی ہے۔"),
                RuleExample("The sun shines bright in the sky.", "سورج آسمان میں چمکتا ہے۔ (sun اور sky منفرد ہیں)"),
                RuleExample("He is an honest police officer.", "وہ ایک ایماندار پولیس آفیسر ہے۔ (honest کا آغاز واؤل آواز سے ہے)"),
                RuleExample("The car we rented broke down.", "وہ گاڑی جو ہم نے کرائے پر لی تھی خراب ہو گئی۔ (مخصوص گاڑی)")
            ),
            quiz = listOf(
                MCQ("I bought ______ new computer yesterday.", listOf("a", "an", "the", "no article"), 0, "Consonant sound 'new' takes indefinite article 'a'."),
                MCQ("She wants to eat ______ apple.", listOf("an", "a", "the", "no article"), 0, "Vowel sound 'apple' takes 'an'."),
                MCQ("He is ______ honest man.", listOf("an", "a", "the", "no article"), 0, "Vowel sound 'honest' (silent h) takes 'an'."),
                MCQ("______ Sun rises in the east.", listOf("The", "A", "An", "no article"), 0, "Unique celestial object takes definite article 'The'."),
                MCQ("I love playing ______ guitar.", listOf("the", "a", "an", "no article"), 0, "Musical instruments take 'the' when playing them."),
                MCQ("He lives in ______ big house in Lahore.", listOf("a", "an", "the", "no article"), 0, "Consonant sound 'big' takes 'a'."),
                MCQ("______ milk is good for health.", listOf("no article", "A", "An", "The"), 0, "General uncountable nouns do not take articles."),
                MCQ("He went to ______ university yesterday.", listOf("a", "an", "the", "no article"), 0, "Consonant sound 'yoo-niversity' takes 'a'."),
                MCQ("We traveled by ______ train.", listOf("no article", "a", "an", "the"), 0, "Idiomatic expressions 'by train/car/bus' take no article."),
                MCQ("This is ______ best book I have ever read.", listOf("the", "a", "an", "no article"), 0, "Superlatives ('best') always take the definite article 'the'.")
            )
        )
    )
}
