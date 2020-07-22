package kotlinmud.attributes.type

import kotlinmud.attributes.dao.AttributesDAO

interface HasAttributes {
    val attributes: AttributesDAO
}
