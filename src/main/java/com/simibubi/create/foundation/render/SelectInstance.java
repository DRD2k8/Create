package com.simibubi.create.foundation.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.jozufozu.flywheel.api.instance.Instance;
import com.jozufozu.flywheel.api.instance.Instancer;

public class SelectInstance<D extends Instance> {

	final List<Instancer<D>> models;

	ModelSelector selector;

	private int last = -1;
	@Nullable
	private D current;

	public SelectInstance(ModelSelector selector) {
		this.models = new ArrayList<>();
		this.selector = selector;
	}

	public SelectInstance<D> addModel(Instancer<D> model) {
		models.add(model);
		return this;
	}

	public SelectInstance<D> update() {
		int i = selector.modelIndexToShow();

		if (i < 0 || i >= models.size()) {
			if (current != null) {
				current.handle().setDeleted();
				current = null;
			}
		} else if (i != last) {
			if (current != null) current.handle().setDeleted();

			current = models.get(i)
					.createInstance();
		}

		last = i;
		return this;
	}

	public Optional<D> get() {
		return Optional.ofNullable(current);
	}

	public void delete() {
		if (current != null) current.handle().setDeleted();
	}

	public interface ModelSelector {
		int modelIndexToShow();
	}
}
