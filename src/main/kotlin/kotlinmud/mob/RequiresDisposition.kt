package kotlinmud.mob

import kotlinmud.mob.type.Disposition

interface RequiresDisposition {
    val dispositions: List<Disposition>
}
