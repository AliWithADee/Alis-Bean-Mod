package io.github.aliwithadee.alisbeanmod.core.energy;

public class BeanEnergyConsumerStorage extends BeanEnergyStorage {

    public BeanEnergyConsumerStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive, 0);
    }

    // Consumers can consume energy
    // Consumers will never create energy
    @Override
    public boolean canConsume() {
        return true;
    }

    // Consumers will never have energy extracted
    @Override
    public boolean canExtract() {
        return false;
    }
}
