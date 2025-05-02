package erengee.daid.mixin;

import erengee.daid.config.ModConfig;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
	@Shadow private ServerPlayerEntity owner;

	@Inject(at = @At("HEAD"), method = "grantCriterion", cancellable = true)
	public void stopUpdatesInSpecifiedDimensions(AdvancementEntry advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
		String dimensionId = owner.getEntityWorld().getDimensionEntry().getIdAsString();
		if (advancement.value().display().isPresent() && ModConfig.isDimensionBlocked(dimensionId)) {
			cir.setReturnValue(false);
		}
	}
}