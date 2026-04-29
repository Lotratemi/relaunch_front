package com.codingfactory.relaunch.ui.profiling

enum class QuestionType { CHOICE, SCALE }

data class ProfilingQuestion(
    val text: String,
    val type: QuestionType,
    val options: List<String> = emptyList(),
    val scaleLeft: String? = null,
    val scaleRight: String? = null
)

val PROFILING_QUESTIONS = listOf(
    ProfilingQuestion(
        text = "Face à un défi, quelle est votre première réaction ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "J'analyse les données disponibles",
            "J'en discute avec mon entourage",
            "Je visualise la solution idéale",
            "Je passe directement à l'action"
        )
    ),
    ProfilingQuestion(
        text = "Qu'est-ce qui vous motive le plus au quotidien ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Atteindre des objectifs ambitieux",
            "Créer des liens forts avec les autres",
            "Approfondir ma compréhension d'un sujet",
            "Innover et explorer de nouvelles idées"
        )
    ),
    ProfilingQuestion(
        text = "Comment vous situez-vous par rapport à la planification ?",
        type = QuestionType.SCALE,
        scaleLeft = "Je vis dans le présent",
        scaleRight = "Je planifie tout à l'avance"
    ),
    ProfilingQuestion(
        text = "Dans un groupe, quel rôle prenez-vous naturellement ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Leader / meneur",
            "Médiateur / arbitre",
            "Expert / conseiller",
            "Exécutant / réalisateur"
        )
    ),
    ProfilingQuestion(
        text = "Comment réagissez-vous face à l'échec ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "J'analyse ce qui s'est passé",
            "Je rebondis et repars de l'avant",
            "Je cherche du soutien autour de moi",
            "J'accepte et passe naturellement à la suite"
        )
    ),
    ProfilingQuestion(
        text = "Votre rapport aux règles et processus ?",
        type = QuestionType.SCALE,
        scaleLeft = "Je préfère innover et les contourner",
        scaleRight = "Je les respecte et les structure"
    ),
    ProfilingQuestion(
        text = "Que signifie le succès pour vous ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Accomplir mes objectifs personnels",
            "Avoir un impact positif sur les autres",
            "Être reconnu pour mon expertise",
            "Créer quelque chose d'unique et nouveau"
        )
    ),
    ProfilingQuestion(
        text = "Comment prenez-vous vos décisions importantes ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "En suivant mon intuition",
            "En analysant les faits et chiffres",
            "En consultant mon entourage",
            "En évaluant risques et opportunités"
        )
    ),
    ProfilingQuestion(
        text = "À quel rythme préférez-vous progresser ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Vite — j'ai besoin de voir des résultats rapides",
            "Méthodiquement — je préfère bien faire les choses",
            "Au fil des rencontres et des échanges",
            "Librement, sans contrainte de calendrier"
        )
    ),
    ProfilingQuestion(
        text = "À quel point êtes-vous à l'aise avec l'expression de vos émotions ?",
        type = QuestionType.SCALE,
        scaleLeft = "Très difficile pour moi",
        scaleRight = "Très naturel et fluide"
    ),
    ProfilingQuestion(
        text = "Face à un changement important, vous…",
        type = QuestionType.CHOICE,
        options = listOf(
            "L'anticipez et vous préparez activement",
            "L'accueillez avec curiosité",
            "Avez besoin de temps pour vous adapter",
            "Préférez la stabilité et cherchez à le limiter"
        )
    ),
    ProfilingQuestion(
        text = "Votre entourage vous décrit le plus souvent comme…",
        type = QuestionType.CHOICE,
        options = listOf(
            "Ambitieux(se) et déterminé(e)",
            "Empathique et à l'écoute",
            "Rigoureux(se) et fiable",
            "Créatif(ve) et original(e)"
        )
    ),
    ProfilingQuestion(
        text = "Comment rechargez-vous vos batteries ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Seul(e), dans le calme et la solitude",
            "Avec des proches et des activités sociales",
            "En pratiquant une passion ou un hobby",
            "En travaillant sur un projet stimulant"
        )
    ),
    ProfilingQuestion(
        text = "Quelle affirmation vous représente le mieux dans votre travail ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Je suis là pour décider et avancer",
            "Je suis là pour comprendre et analyser",
            "Je suis là pour relier et harmoniser",
            "Je suis là pour créer et imaginer"
        )
    ),
    ProfilingQuestion(
        text = "Quel aspect du coaching vous attire le plus ?",
        type = QuestionType.CHOICE,
        options = listOf(
            "Définir une vision et une stratégie claire",
            "Améliorer mes performances et ma productivité",
            "Mieux me comprendre et gérer mes émotions",
            "Améliorer mes relations et ma communication"
        )
    )
)