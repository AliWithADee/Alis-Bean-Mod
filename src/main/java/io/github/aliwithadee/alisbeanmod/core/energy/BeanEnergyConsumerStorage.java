package io.github.aliwithadee.alisbeanmod.core.energy;

public class BeanEnergyConsumerStorage extends BeanEnergyStorage {

    private final boolean canCreate;

    public BeanEnergyConsumerStorage(int capacity, int maxReceive, boolean canCreate) {
        super(capacity, maxReceive, 0);
        this.canCreate = canCreate;
    }

    // Consumers can consume energy
    @Override
    public boolean canConsume() {
        return true;
    }

    // SOME Consumers will create their own energy, others will not
    @Override
    public boolean canCreate() {
        return this.canCreate;
    }

    // Consumers will never have energy extracted
    @Override
    public boolean canExtract() {
        return false;
    }
}
