package com.example

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.micronaut.runtime.Micronaut.run
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

val id: UUID = UUID.randomUUID()
val date: LocalDate = LocalDate.parse("2023-09-28", DateTimeFormatter.ISO_LOCAL_DATE)

@MappedEntity("event")
data class Event(
	@field:Id val id: UUID,
	val date: LocalDate
)

@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventRepository: CrudRepository<Event, UUID> {}

/**
 * Saves an object to database and read its back.
 * BUG: LocalDate is always saved incorrectly inside the DB (off by 1 day)
 */
fun main(args: Array<String>) {
	val context = run(*args)
	val repo = context.getBean(EventRepository::class.java)

	println("*** saving to db ==> $date")
	val newEvent = Event(id, date)
	repo.save(newEvent)

	// read back from database
	val eventFromDb = repo.findById(id)
	println("*** date from db ===> ${eventFromDb.get().date}")
}

