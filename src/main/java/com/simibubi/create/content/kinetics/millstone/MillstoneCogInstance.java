package com.simibubi.create.content.kinetics.millstone;

import com.jozufozu.flywheel.api.model.Model;
import com.jozufozu.flywheel.api.visualization.VisualizationContext;
import com.jozufozu.flywheel.lib.model.Models;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;

public class MillstoneCogInstance extends SingleRotatingInstance<MillstoneBlockEntity> {

    public MillstoneCogInstance(VisualizationContext materialManager, MillstoneBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

	@Override
	protected Model model() {
		return Models.partial(AllPartialModels.MILLSTONE_COG);
	}
}
