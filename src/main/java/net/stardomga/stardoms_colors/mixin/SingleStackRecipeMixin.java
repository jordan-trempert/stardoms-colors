package net.stardomga.stardoms_colors.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SingleStackRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleStackRecipe.class)
public abstract class SingleStackRecipeMixin {
    @Inject(
            method = "craft(Lnet/minecraft/recipe/input/SingleStackRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void copyInputComponents(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup registries, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack output = cir.getReturnValue();
        if(input.item().getComponents().contains(DataComponentTypes.DYED_COLOR)){
            output.set(DataComponentTypes.DYED_COLOR, input.item().get(DataComponentTypes.DYED_COLOR));
        }
    }
}