package edu.itvo.biblioteca

import android.os.Build
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class SistemaBibliotecaTest {

    private lateinit var library: Library
    private lateinit var loanService: LoanService
    private lateinit var mainUser: User

    @Before
    fun setUp() {
        library = Library()
        loanService = LoanService(library)

        mainUser = User(name = "Bibiana Rubi", id = 1)

        val initialCatalog = listOf(
            Book("SOLID", "Ambrosio Cardoso Jimenez", "101"),
            Book("OOP", "Author X", "102"),
            Book("CISCO", "Francisco Javier Trujillo Lopez", "103"),
            Book("JAVA", "Benedicto Ramirez Santiago", "999")
        )

        library.books.addAll(initialCatalog)
        library.users.add(mainUser)
    }

    @Test
    fun `validate that a user cannot have more than 3 books`() {
        val books = library.books

        // Simular préstamo de los primeros 3 libros
        for (i in 0..2) {
            loanService.loanBook(books[i], mainUser)
        }

        // Verificar que el usuario tiene 3 préstamos
        assertEquals("User should have 3 loans", 3, mainUser.loans.size)

        // Intentar autorizar el 4to libro
        val fourthBook = books[3]
        val authorized = loanService.authorizeLoan(mainUser.id, fourthBook.isbn)

        println("--- REPORT: LOAN LIMIT ---")
        println("User: ${mainUser.name}")
        println("Book: ${fourthBook.title} | Author: ${fourthBook.author}")
        println("Result: ${if (authorized) "APPROVED" else "REJECTED (Limit of 3 reached)"}")
        println("---------------------------\n")

        assertFalse("The system should not allow more than 3 books per user", authorized)

        // Verificar que el libro aún está disponible
        assertTrue("Fourth book should still be available", fourthBook.available)
    }

    @Test
    fun `should not loan a book that is no longer available`() {
        val targetBook = library.books.first { it.isbn == "999" }
        val secondUser = User(name = "Rubi", id = 2)
        library.users.add(secondUser)

        // Primer usuario toma el libro
        loanService.loanBook(targetBook, mainUser)

        // Verificar que el libro no está disponible
        assertFalse("Book should be unavailable after loan", targetBook.available)

        // Segundo usuario intenta tomar el mismo libro
        val authorized = loanService.authorizeLoan(secondUser.id, targetBook.isbn)

        println("--- REPORT: AVAILABILITY ---")
        println("Book: ${targetBook.title} | Author: ${targetBook.author}")
        println("Loan attempt by: ${secondUser.name}")
        println("Result: ${if (authorized) "SUCCESS" else "FAILED (Book is already on loan)"}")
        println("-----------------------------\n")

        assertFalse("Cannot loan a book that is already on loan", authorized)

        // Verificar que el segundo usuario no tiene préstamos
        assertEquals("Second user should have 0 loans", 0, secondUser.loans.size)
    }

    @Test
    fun `show available books in console`() {
        // Tomar el libro OOP para testing
        val oopBook = library.books.first { it.title == "OOP" }
        loanService.loanBook(oopBook, mainUser)

        val availableBooks = library.books.filter { it.available }

        println("--- REPORT: AVAILABLE CATALOG ---")
        availableBooks.forEach { book ->
            println("Title: ${book.title.padEnd(15)} | Author: ${book.author.padEnd(30)} | Status: AVAILABLE")
        }
        println("Total available: ${availableBooks.size}")
        println("----------------------------------\n")

        assertEquals(3, availableBooks.size)
        assertFalse("OOP book should not be available", availableBooks.contains(oopBook))
    }

    @Test
    fun `test return book functionality`() {
        val book = library.books.first()

        // Prestar libro
        loanService.loanBook(book, mainUser)
        assertFalse("Book should be unavailable after loan", book.available)
        assertEquals("User should have 1 loan", 1, mainUser.loans.size)

        // Devolver libro
        loanService.returnBook(book, mainUser)
        assertTrue("Book should be available after return", book.available)
        assertEquals("User should have 0 loans", 0, mainUser.loans.size)
    }
}