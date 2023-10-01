package com.example

import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.DataType
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.model.runtime.convert.AttributeConverter
import io.micronaut.data.repository.CrudRepository
import io.micronaut.runtime.Micronaut.run
import jakarta.inject.Singleton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.exitProcess

val id: UUID = UUID.randomUUID()
val date: LocalDate = LocalDate.parse("2023-09-28", DateTimeFormatter.ISO_LOCAL_DATE)

@MappedEntity("event")
data class Event(
	@field:Id val id: UUID,

	@field:TypeDef(type = DataType.STRING, converter = LocalDateConverter::class)
	val date: LocalDate
)

@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventRepository: CrudRepository<Event, UUID>

@Singleton
class LocalDateConverter: AttributeConverter<LocalDate, String> {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
	override fun convertToPersistedValue(entityValue: LocalDate?, context: ConversionContext): String? {
		return formatter.format(entityValue)
	}

	override fun convertToEntityValue(persistedValue: String?, context: ConversionContext): LocalDate? {
		return LocalDate.parse(persistedValue, formatter)
	}

}

fun main(args: Array<String>) {
	val context = run(*args)
	val repo = context.getBean(EventRepository::class.java)

	println("*** saving to db ==> $date")
	val newEvent = Event(id, date)
	repo.save(newEvent)

	// read back from database
	val eventFromDb = repo.findById(id)
	println("*** date from db ===> ${eventFromDb.get().date}")

	exitProcess(0)
}

