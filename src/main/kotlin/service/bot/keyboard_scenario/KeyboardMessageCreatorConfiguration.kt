package ege.bot.service.bot.keyboard_scenario

import ege.bot.service.bot.keyboard_scenario.add_students_scenario.AddStudentsWayKeyboard
import ege.bot.service.bot.keyboard_scenario.edit_student.EditStudentKeyboard
import ege.bot.service.bot.keyboard_scenario.forget_me.ForgetMeKeyboardItem
import ege.bot.service.bot.keyboard_scenario.start.ChatMode
import ege.bot.service.bot.keyboard_scenario.teacher_print_result.PrintResultKeyboard
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeyboardMessageCreatorConfiguration {

    @Bean
    fun startMessageCreator(): KeyboardMessageCreator<ChatMode> {
        return KeyboardMessageCreator(START_MESSAGE.trimMargin(), ChatMode::class.java)
    }

    @Bean
    fun forgetMeMessageCreator(): KeyboardMessageCreator<ForgetMeKeyboardItem> {
        return KeyboardMessageCreator(FORGET_ME_MESSAGE, ForgetMeKeyboardItem::class.java)
    }

    @Bean
    fun editStudentDataMessageCreator(): KeyboardMessageCreator<EditStudentKeyboard> {
        return KeyboardMessageCreator(EDIT_STUDENT_DATA_TEXT, EditStudentKeyboard::class.java)
    }

    @Bean
    fun addStudentsOptionsMessageCreator(): KeyboardMessageCreator<AddStudentsWayKeyboard> {
        return KeyboardMessageCreator(ADD_STUDENTS_OPTION, AddStudentsWayKeyboard::class.java)
    }

    @Bean
    fun printResultMessageCreator(): KeyboardMessageCreator<PrintResultKeyboard> {
        return KeyboardMessageCreator(CHOOSE_RESULTS_PRINTING, PrintResultKeyboard::class.java)
    }

    companion object {
        const val START_MESSAGE = """
            |Привет!
            |Я бот для автоматизации получения результатов ЕГЭ/ОГЭ по Санкт-Петерургу. Ну или вкратце - твоего удобства.
                
            |*Как пользоваться:*
            |1. Передай мне твои паспортные данные
            |2. Если хочешь, можешь указать псевдоним для паспортных данных
            |3. Когда я получу обновления по твоим результатам - пришлю их тебе.
            |4. Если захочешь удалить твои паспортные данные - всегда можешь это сделать, но в таком случае я не смогу обновлять результаты твоих экзаменов.
                
            |P.S. Если ты опасаешься за сохранность своих приватных данных - весь [код](https://github.com/DFirsa/EgeScraperBot) бота полностью открытый. Можешь его посмотреть самостоятельно: 
            |И не забудь поставить звездочку (я буду знать что делал бота не зря)!
            
            |*Выбери режим в котором тебе нужно получать результаты:*
            
            |`STUDENT` - ты сдаешь экзамен и тебе нужно получать результаты, только для себя
            |`TEACHER` - ты учитель, и нужно получать результаты нескольких людей
        """

        private const val FORGET_ME_MESSAGE = "‼\uFE0F Вы уверены, что хотите удалить информацию об этом чате, " +
                "учениках и их результатах?"

        private const val EDIT_STUDENT_DATA_TEXT = "Выберите данные которые хотите изменить:"

        private const val ADD_STUDENTS_OPTION = "Выберите способ добавления учеников:"

        private const val CHOOSE_RESULTS_PRINTING = "Выберите как хотите получить результаты: "
    }
}