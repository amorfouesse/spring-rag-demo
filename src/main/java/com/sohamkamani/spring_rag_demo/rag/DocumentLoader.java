package com.sohamkamani.spring_rag_demo.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentLoader implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public DocumentLoader(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("Suppression des anciens vecteurs...");
        jdbcTemplate.execute("TRUNCATE TABLE vector_store CASCADE");

        //chunquer en une seul phrase permet d'avoir un sens très préçis.
        List<Document> documents = List.of(
                new Document(
                        """
                                    Les Orks sont les Xenos les plus belliqueux et les plus communs qui infestent les étoiles. Personne ne sait combien il y a d’Orks, mais il semble qu’au cours des millénaires, ils ont migré ou se sont battus pour atteindre les quatre coins de la galaxie. Lorsque les humains ont commencé à voyager dans les étoiles et à coloniser les systèmes stellaires voisins, ils n’ont pas tardé à rencontrer cette menace à la peau verte. L’humanité, et plus tard l’Imperium, ont été constamment en guerre avec les Orks depuis lors.
                                    Innombrables et habités du besoin impérieux de combattre et conquérir, les Orks et leurs semblables sont une menace pour chaque empire, forteresse et race du 41e Millénaire. Dans le fracas de la guerre, du cœur de la galaxie jusqu’aux lointains Astres Fantômes, leurs empires naissent et meurent sans cesse. Heureusement, la plupart sont éphémères et disparaissent rapidement dans un tourbillon de violence. Si un jour la race des Orks mettait de côté ses différends pour s’unir, ils pourraient écraser toute résistance et noyer les races civilisées sous une mer de sang, mais leur appétit insatiable pour la violence fait tout autant leur force que leur faiblesse : au cours de l’Histoire, leurs tribus ont passé plus de temps à s’entre-tuer qu’à collaborer pour vaincre leurs ennemis, ces guerres civiles n’ayant pour seul objectif que la survie du plus fort.
                                    À travers tout l’Imperium, la colossale machine de propagande du Munitorum ne se fatigue pas de présenter les Peaux-Vertes comme des êtres inférieurs, dur à la détente, bestiaux et dotés d’une compréhension totalement rudimentaire de ce qu’il faut faire pour gagner une guerre sur la longueur. Le minuscule cerveau d’un Ork est incapable de faire une analyse tactique et de réagir en fonction, ainsi, pour l’essentiel, la propagande n’est pas loin de la vérité. Un Ork y arrive par ses muscles, sa résistance et par sa sauvagerie à l’état pur, pas grand-chose d’autre. [1]
                                    Les forces de combat des Orks (c'est-à-dire la totalité d'entre eux, puisque les Peaux-Vertes ne font pas de distinction entre les rôles civils et militaires) sont organisées en tribus, clans et groupes de combat. Ceux-ci peuvent prendre de nombreuses formes différentes, car il n'y a pas d'organisation formelle au sein d’une armée Ork, comme le ferait un chapitre Space Marine ou un régiment de la Garde Impériale. Pour un œil non averti, une armée Ork ressemble à une horde en haillons indisciplinée, attaquant sans réfléchir tout ce qui se trouve sur son chemin. L’œil non averti ne se tromperait pas entièrement, car les Orks ne se soucient guère de savoir qui ils combattent, mais seulement de se battre. L’Ordo Xenos a une longue histoire d’étude de la société et de la culture des Peaux-Vertes, et a identifié des variations plus subtiles au sein des armées Ork.
                                    Chaque groupe Ork est uni par la puissance et la force de son Boss de Guerre, et le caractère de ce dernier détermine le caractère du groupe qu’il dirige. Un chef Goff dirigera une Bande composée principalement de la tribu violente et coriace des Goffs (mais pas exclusivement). En temps de guerre, les tribus se mélangent librement, les Bandes rejoignant (ou étant forcées de rejoindre) la Bande de Guerre locale dominante.[2]
                                    Parfois, un chef Ork parvient à vaincre ses rivaux et à unir plusieurs tribus. Son succès attire rapidement à lui de plus en plus d’Orks avides de profiter du carnage, et bientôt une Waaagh! se rassemble. Il s’agit à la fois d’une migration à grande échelle et d’une guerre sainte qui ravagera des systèmes entiers, et quand l’une d’elle survient, la galaxie tout entière est en danger.
                                    Et jamais les Waaagh! n’ont été aussi nombreuses qu’aujourd’hui.
                                    Les Orks sont des créatures laides et agressives. Ils forment la caste dominante de leur race, qui comprend également les Gretchins et les Snotlings. Ils règnent sur leur parodie de civilisation avec une poigne de fer, se considèrent comme la race la plus puissante et la plus redoutable de la galaxie, et mettent un point d’honneur à tuer tous ceux qu’ils rencontrent pour le prouver.
                                    L’Ork moyen est de constitution extrêmement robuste. Il fait la taille d’un humain, mais il est en fait beaucoup plus grand s’il se redresse au lieu de conserver sa posture voûtée habituelle. Il est aussi extrêmement robuste, et ses muscles sont redoutablement puissants. Les bras d’un Ork sont longs et simiesques, et ses mains touchent presque le sol quand il se déplace. Ces dernières sont dotées de doigts griffus capables d’égorger une proie sans difficultés.
                                    Le crâne d’un Ork est incroyablement épais et peut absorber des impacts qui fractureraient celui d’un humain. Ses arcades sourcilières proéminentes cachent des yeux rouges qui trahissent une soif de sang immense. Des crocs si gros qu’ils seraient effrayants même sur un prédateur de plus grande taille saillent hors de sa mâchoire. Sa peau est verte et possède la consistance du cuir. Elle est toujours couturée de cicatrices et de plaies, et recouverte d’ulcères et de parasites. Le pire chez un Ork reste toutefois son odeur, qu’on a souvent assimilée à celle d’un Grox maladif agonisant dans ses excréments.
                                    Un Ork est si robuste qu’il peut survivre aux blessures les plus terribles. Il ne ressent presque pas la douleur, ce qui lui permet de continuer le combat en dépit des mutilations les plus handicapantes, et parfois même pendant un court laps de temps après avoir subi des blessures fatales. Certains érudits qui étudient les Orks supposent que cela explique leur sens de l’humour violent : puisque la douleur les affecte peu, ils s’amusent beaucoup des réactions de leurs victimes lorsqu’ils les hachent menu. Les cris de celles-ci contrastent horriblement avec les grognements gutturaux qui émanent des gorges de leurs tortionnaires, et les gloussements cruels de leurs congénères plus petits.
                                    Les capacités régénératrices des Peaux-Vertes sont telles que si un Ork est amputé d’un membre, il suffit de le lui recoudre grossièrement pour qu’il en retrouve l’usage, et qu’il retourne au combat à peine quelques heures plus tard, légèrement désorienté mais tout aussi féroce qu’avant. Seules les blessures les plus handicapantes peuvent mettre un Ork hors de combat durablement, et on dit que brûler leurs cadavres est le seul moyen de s’assurer de leur mort.
                                    De nombreuses théories avancent que les Orks partagent des traits communs aux formes de vie algoïdes et fongiques, ce qui leur confère leur remarquable constitution. La couleur verte des Orks serait due à une algue qui entre dans une partie de la composition de leur structure cellulaire. Elle reforme et répare les tissus à une vitesse extraordinaire et explique en grande partie la résistance de leur métabolisme. Les observateurs qui soutiennent cette théorie avancent que la tête d’un Ork peut survivre un certain temps après avoir été séparée de son corps. De fait, la transplantation de tête fait partie du répertoire de nombreux Médikos.
                                    Lorsqu’il parle (toujours de façon laconique), un Ork s’exprime lentement, la voix chargée de salive, de jurons gutturaux et de reniflements bruyants qui annoncent ses intentions meurtrières. Il utilise des mots simples qui dénotent sa façon de penser rudimentaire, car les Peaux-Vertes ne suivent que la loi du plus fort. Ils pensent que le faible doit souffrir, et nul Ork n’a jamais remis cela en cause depuis des millénaires. C’est d’ailleurs cette croyance qui les rend si redoutables, car ils n’arrêteront jamais de faire la guerre contre le reste de la galaxie.
                                    L’espérance de vie des Orks est inconnue, et il a été supposé qu’ils continuent simplement à vivre jusqu’à ce qu’ils périssent au combat, devenant de plus en plus grands et couverts de cicatrices au fil des années, des décennies, voire des siècles de guerre. Certains Orks, portés par les marées incertaines du Warp, pourraient bien être des vétérans centenaires, ayant à leur actif l’expérience d’une vie entière de violence implacable, ainsi qu’une taille et une force inégalées par tout être humain non augmenté, et une résistance qui surpasse parfois celle des puissants guerriers de l’Adeptus Astartes.
                                    Citation d'ork : «Lé zoms sont roz’ et mous, pas durs et verts komme lé boy. Y sont tous d’la même taille, alors y s’battent san zarrêt sur ki va kommander, passke leur seul moyen d’le savoir c’est avek des médailles et des zuniformes et des bidul’. Kand y’en a un ki veut kommander lé zautres, y dit : "chuis pas pareil ke vous, alors faut m’obéir," ou bien "j’sais des truks mieux k’vou zaut’, alors ékoutez-moi." Le pire, cé ke y’en a ke la moitié ki le croient, alors y faut ki pète la tête aux autres, ou ki s’taille en courant. Moi j’trouve ça débil’, mais au moins pendant ki s’chamaillent pour savoir cé ki le boss, lé zorks peuvent arriver sans crier gare pour tous les tabasser. »
                                """));
        // chunkSize : 9 378 caractères ≈ 2 350 Tokens. ( 1 token = 4 caracteres),
        // ici on met 400 tokens car mon model d'embedding me limite (512 tokens max), j'aurai alors jusqu'a 6 à 7 documents(chunks)
        // minChunkSizeChars : Si un morceau fait moins de 300 caractères, on le jete car trop petit
        // minChunkLengthToEmbed : empeche de vectoriser des morceaux trop petits ( - de 100 chunk) qui n'on pas de sens sémantique
        // maxNumChunks : le nombre max de chunks, attention si dépassé ignore les infos restantes
        TokenTextSplitter splitter = new TokenTextSplitter(400, 300, 100, 50, true);
        var chunkDocuments = splitter.apply(documents);
        vectorStore.add(chunkDocuments);
        System.out.println("Documents loaded into VectorStore.");
    }
}
