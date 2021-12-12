package io.github.aliwithadee.alisbeanmod.core.energy;

public class BeanEnergyProducerStorage extends BeanEnergyStorage {

    public BeanEnergyProducerStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    // Producers can create energy
    // Producers will never consume energy
    @Override
    public boolean canCreate() {
        return true;
    }

    // Producers will never receive energy
    @Override
    public boolean canReceive() {
        return false;
    }
}
