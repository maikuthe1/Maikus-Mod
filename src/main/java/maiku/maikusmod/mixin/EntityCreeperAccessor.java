package maiku.maikusmod.mixin;

import net.minecraft.core.entity.monster.EntityCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EntityCreeper.class, remap = false)
public interface EntityCreeperAccessor {

	@Accessor("timeSinceIgnited")
	int ignitedTime();

}
