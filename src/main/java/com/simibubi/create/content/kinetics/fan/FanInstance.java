package com.simibubi.create.content.kinetics.fan;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

import com.jozufozu.flywheel.api.visualization.VisualizationContext;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class FanInstance extends KineticBlockEntityInstance<EncasedFanBlockEntity> {

    protected final RotatingInstance shaft;
    protected final RotatingInstance fan;
    final Direction direction;
    private final Direction opposite;

    public FanInstance(VisualizationContext materialManager, EncasedFanBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		direction = blockState.getValue(FACING);

		opposite = direction.getOpposite();
		shaft = instancerProvider.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF, opposite), RenderStage.AFTER_BLOCK_ENTITIES).createInstance();
		fan = instancerProvider.instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.ENCASED_FAN_INNER, opposite), RenderStage.AFTER_BLOCK_ENTITIES)
				.createInstance();

		setup(shaft);
		setup(fan, getFanSpeed());
	}

    private float getFanSpeed() {
        float speed = blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(fan, getFanSpeed());
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, fan);
    }

    @Override
    public void remove() {
        shaft.delete();
        fan.delete();
    }
}
