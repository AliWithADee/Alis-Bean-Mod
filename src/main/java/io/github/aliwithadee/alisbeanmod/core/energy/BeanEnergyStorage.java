package io.github.aliwithadee.alisbeanmod.core.energy;

import net.minecraftforge.energy.EnergyStorage;

public class BeanEnergyStorage extends EnergyStorage {

    public BeanEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    // COMMON

    // Used to tell the Block Entity that the energy storage has been changed
    protected void onEnergyChanged() {

    }

    public boolean canCreate() {
        return false;
    }

    public boolean canConsume() {
        return false;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean canAccept(int energy) {
        return this.capacity - this.energy >= energy;
    }

    // ENERGY PRODUCERS

    public void createEnergy(int energy) {

        if (!canCreate())
            return;

        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getMaxEnergyStored();
        }
        onEnergyChanged();
    }

    // ENERGY CONSUMERS

    public void consumeEnergy(int energy) {

        if (!canConsume())
            return;

        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
        onEnergyChanged();
    }
}
