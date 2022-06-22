package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController
class MessageResource(val service: MessageService){
	@GetMapping
	fun index(): List<Message> = service.findMessages()

	@GetMapping("all")
	fun listAllMessages(): List<Message> = service.findAllMessages()

	@PostMapping
	fun postMessage(@RequestBody message: Message){
		service.post(message)
	}

	@DeleteMapping
	fun deleteMessage(){
		service.deleteMessages()
	}

	@DeleteMapping("id/{id}")
	fun deleteMessage(@PathVariable("id") id: String){
		service.deleteMessageById(id)
	}
}

@Service
class MessageService(val db: MessageRepository){
	fun findMessages(): List<Message> = db.findMessages()

	fun findAllMessages(): List<Message> = db.findAll()
	fun post(message: Message){
		db.save(message)
	}

	fun deleteMessages(){
		db.deleteAll()
	}

	fun deleteMessageById(id :String){
		db.deleteById(id)
	}
}

@Repository
interface MessageRepository : CrudRepository<Message, String>{
	@Query("select * from messages")
	fun findMessages(): List<Message>
	override fun findAll(): List<Message>

	override fun deleteAll(): Unit
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)
