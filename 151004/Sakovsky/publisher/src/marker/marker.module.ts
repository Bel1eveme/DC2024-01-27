import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Marker } from '../entities/Marker';
import { MarkerController } from './marker.controller';
import { MarkerService } from './marker.service';

@Module({
  imports: [TypeOrmModule.forFeature([Marker])],
  providers: [MarkerService],
  controllers: [MarkerController]
})
export class MarkerModule {}
