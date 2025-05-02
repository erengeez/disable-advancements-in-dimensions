package erengee.daid.mixin;

import erengee.daid.config.ModConfig;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public class AbstractCriterionMixin {
	@Inject(at = @At("HEAD"), method = "trigger", cancellable = true)
	public <T> void stopUpdatesInSpecifiedDimensions(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci) {
		String dimensionId = player.getEntityWorld().getDimensionEntry().getIdAsString();
		if (ModConfig.isDimensionBlocked(dimensionId)) {
			ci.cancel();
		}
	}
}