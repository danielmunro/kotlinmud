package kotlinmud.service

import io.github.serpro69.kfaker.Faker
import kotlinmud.item.factory.itemBuilder
import kotlinmud.item.model.ItemBuilder
import kotlinmud.mob.dao.MobDAO

class FixtureService {
    private var mobs = 0
    private var items = 0
    val faker = Faker()

    fun createMob(): MobDAO {
        return MobDAO.new {
            name = faker.name.name()
        }
    }

    fun createItemBuilder(name: String = faker.book.title()): ItemBuilder {
        items++
        return itemBuilder(items, name)
            .description("$name is here")
    }
}
