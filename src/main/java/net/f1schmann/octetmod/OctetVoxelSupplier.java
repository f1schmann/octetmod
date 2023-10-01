package net.f1schmann.octetmod;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OctetVoxelSupplier {
    public static final VoxelShape[] VoxelOctets = makeVoxelOctets();
    private static final VoxelShape[] VoxelShapes = new VoxelShape[256];

    private static VoxelShape[] makeVoxelOctets() {
        VoxelShape[] voxelOctets = new VoxelShape[8];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                for(int k = 0; k < 2; k++)
                    voxelOctets[i*4+j*2+k] = Shapes.box(i/2., j/2., k/2., (i+1)/2., (j+1)/2., (k+1)/2.);
        return voxelOctets;
    }

    public static VoxelShape getVoxelShape(int index){
        if (VoxelShapes[index] == null){
            VoxelShape shape = Shapes.empty();
            for (int mask = 0; mask < 8; mask++)
                if ((index & (1<<mask)) != 0)
                    shape = Shapes.or(shape, VoxelOctets[mask]);
            VoxelShapes[index] = shape;
        }
        return VoxelShapes[index];
    }
}
