import de.nosswald.api.utils.TickCounter
import de.nosswald.api.utils.TickTimeFormatter
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TickTimeTest {
    private fun ticksToTime() = Stream.of(
        arguments(0, "00.000", TimeUnit.SECONDS),
        arguments(1, "00.050", TimeUnit.SECONDS),
        arguments(20, "01.000", TimeUnit.SECONDS),
        arguments(1199, "59.950", TimeUnit.SECONDS),
        arguments(1200, "01.00.000", TimeUnit.MINUTES),
        arguments(71999, "59.59.950", TimeUnit.MINUTES),
        arguments(72000, "01.00.00.000", TimeUnit.HOURS),
    )
    @ParameterizedTest
    @MethodSource("ticksToTime")
    fun `Formatting works properly`(ticks: Long, time: String, unit: TimeUnit) {
        assertEquals(time to unit, TickTimeFormatter.format(ticks))
    }

    @Test
    fun `Counter starts at zero`() {
        val counter = TickCounter()

        counter.start()

        assertEquals(0, counter.ticks)
    }

    @Test
    fun `Counter increments properly`() {
        val counter = TickCounter()

        counter.start()
        repeat(5) { counter.tick() }

        assertEquals(5, counter.ticks)
    }

    @Test
    fun `Counter stops properly`() {
        val counter = TickCounter()

        counter.start()
        repeat(5) { counter.tick() }

        assertEquals(5, counter.stop())
        assertEquals(0, counter.ticks)
    }
}
