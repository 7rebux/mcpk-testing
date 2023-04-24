import de.nosswald.api.utils.facing
import org.bukkit.Location
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FacingDirectionTest {
    private fun yawToDirection() = Stream.of(
        arguments(locationWithYaw(0.0F), "Z+"),
        arguments(locationWithYaw(90.0F), "X-"),
        arguments(locationWithYaw(180.0F), "Z-"),
        arguments(locationWithYaw(270.0F), "X+"),
        arguments(locationWithYaw(44.9F), "Z+"),
        arguments(locationWithYaw(45.1F), "X-"),
        arguments(locationWithYaw(-190.0F), "Z-"),
    )

    private fun locationWithYaw(yaw: Float) = Location(null, 0.0, 0.0, 0.0, yaw, 0.0F)

    @ParameterizedTest
    @MethodSource("yawToDirection")
    fun `Facing returns correct direction`(location: Location, direction: String) {
        assertEquals(direction, location.facing())
    }
}
