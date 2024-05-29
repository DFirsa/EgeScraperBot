package ege.bot.model

/**
 * State of specific chat
 */
enum class ChatState {

    /**
     * Start scenario
     */
    START,

    /**
     * Menu scenario
     */
    MENU,

    /**
     * Forget me scenario
     */
    FORGET_ME,

    /**
     * Add students via keyboard state
     */
    ADD_STUDENT,

    /**
     * Menu for editing information about students
     */
    STUDENTS_MENU,

    /**
     * Menu for choosing data for one student
     */
    EDIT_STUDENT_MENU,

    /**
     * State when editing year from chat
     */
    EDIT_YEAR,

    /**
     * State when passport series is editing from chat
     */
    EDIT_PASSPORT_SERIES,

    /**
     * State when passport number is editing from chat
     */
    EDIT_PASSPORT_NUMBER,

    /**
     * State for choosing students which will be deleted
     */
    CHOOSE_STUDENT_FOR_DELETION,

    /**
     * State to handle keyboard for choosing way how to input data
     */
    CHOOSE_WAY_TO_ADD_STUDENTS,

    /**
     * Listening for excel file with new students
     */
    LISTENING_FOR_NEW_STUDENTS_EXCEL,

    /**
     * Listening for excel file with updated students info
     */
    LISTENING_FOR_UPDATED_STUDENTS,

    /**
     * Menu for teacher with options how to print results
     */
    PRINT_RESULT_MENU,

    /**
     * Edit surname state
     */
    EDIT_SURNAME
}