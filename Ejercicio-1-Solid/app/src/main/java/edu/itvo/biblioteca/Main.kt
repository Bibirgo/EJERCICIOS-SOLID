package edu.itvo.biblioteca

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Book(
    val title: String,
    val author: String,
    val isbn: String,
    var available: Boolean = true
)

data class User(
    val name: String,
    val id: Int,
    val loans: MutableList<Loan> = mutableListOf()
)

data class Loan(
    val book: Book,
    val user: User,
    val loanDate: LocalDate,
    var returnDate: LocalDate? = null
)

class Library {
    val books = mutableListOf<Book>()
    val users = mutableListOf<User>()
    val loans = mutableListOf<Loan>()
}

class LoanService(val library: Library) {

    fun authorizeLoan(userId: Int, isbn: String): Boolean {
        val user = library.users.find { it.id == userId } ?: return false
        val availableBook = library.books.any { it.isbn == isbn && it.available }
        return user.loans.size < 3 && availableBook
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loanBook(book: Book, user: User) {
        val loan = Loan(
            book = book,
            user = user,
            loanDate = LocalDate.now()
        )
        library.loans.add(loan)
        book.available = false
        user.loans.add(loan)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun returnBook(book: Book, user: User) {
        val loan = library.loans.find {
            it.book == book && it.user == user && it.returnDate == null
        }
        loan?.let {
            it.returnDate = LocalDate.now()
            user.loans.remove(it)
            book.available = true
        }
    }
}

class LibraryReport(val library: Library) {
    fun showAvailableBooks() {
        println("List of available books:\n")
        library.books.filter { it.available }.forEach { println(it) }
    }

    fun showBorrowedBooks() {
        println("List of borrowed books:\n")
        library.books.filter { !it.available }.forEach { println(it) }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val library = Library()
    library.users.add(User(name = "Ambrosio Cardoso", id = 1))

    library.books.add(Book(
        title = "Aprendiendo Kotlin en 21 días",
        author = "Roman Leobardo",
        isbn = "AAA111",
        available = true
    ))
    library.books.add(Book(
        title = "Algorithms for Functional Programming",
        author = "John David Stone",
        isbn = "AAA112",
        available = true
    ))
    library.books.add(Book(
        title = "Android Development with Kotlin",
        author = "Marcin Moskala",
        isbn = "AAA113",
        available = true
    ))
    library.books.add(Book(
        title = "Effective Kotlin",
        author = "Marcin Moskala",
        isbn = "AAA114",
        available = true
    ))
    library.books.add(Book(
        title = "Kotlin Coroutines",
        author = "Filip Babic",
        isbn = "AAA115",
        available = true
    ))
    library.books.add(Book(
        title = "Kotlin Notes for Professionals",
        author = "Stack OverFlow",
        isbn = "AAA116",
        available = true
    ))

    val isbn = "AAA116"
    val userId = 1

    val loanService = LoanService(library)

    loanService.loanBook(book = library.books[4], user = library.users[0])
    loanService.loanBook(book = library.books[3], user = library.users[0])
    loanService.loanBook(book = library.books[2], user = library.users[0])

    if (loanService.authorizeLoan(userId = userId, isbn = isbn)) {
        loanService.loanBook(
            book = library.books[5],
            user = library.users[0]
        )
        val libraryReport = LibraryReport(library)
        libraryReport.showBorrowedBooks()
    } else {
        println("User $userId cannot be loaned book $isbn")
    }
}