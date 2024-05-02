import { CacheInterceptor, CacheKey } from '@nestjs/cache-manager';
import { Body, Controller, Delete, Get, HttpCode, NotFoundException, Param, ParseIntPipe, Post, Put, UseInterceptors, UsePipes, ValidationPipe } from '@nestjs/common';
import { Marker } from 'src/entities/Marker';
import { markerCacheKeys } from 'src/utils/redis/globalRedis';
import { MarkerRequestToCreate } from '../dto/request/MarkerRequestToCreate';
import { MarkerRequestToUpdate } from '../dto/request/MarkerRequestToUpdate';
import { MarkerResponseTo } from '../dto/response/MarkerResponseTo';
import { MarkerService } from './marker.service';

@UseInterceptors(CacheInterceptor)
@Controller('markers')
export class MarkerController {
    constructor(private readonly markerService: MarkerService ){}

    @CacheKey(markerCacheKeys.markers)
    @Get()
    async getAll(): Promise<MarkerResponseTo[]>{
        const result: MarkerResponseTo[] = [];
        const markers = await this.markerService.getAll();
        for (const marker of markers) {
            const markerDto = new MarkerResponseTo();
            markerDto.id = marker.id;
            markerDto.name = marker.name;
            result.push(markerDto);
        }
        return result;
    }

    @Get(':id')
    async getById(@Param('id', ParseIntPipe) id: number): Promise<Marker>{
        const marker = await this.markerService.getById(id);
        if(!marker){
            throw new NotFoundException('Marker not found');
        }
        return marker;
    }

    @UsePipes(new ValidationPipe())
    @Post()
    async createMarker(@Body() dto: MarkerRequestToCreate): Promise<Marker>{
        const marker =  await this.markerService.createMarker(dto);
        return marker;
    }

    @UsePipes(new ValidationPipe())
    @Put()
    async updateMarker(@Body() markerDto: MarkerRequestToUpdate): Promise<Marker>{
        const marker =  await this.markerService.updateMarker(markerDto);
        return marker;
    }

    @HttpCode(204)
    @Delete(':id')
    deleteMarker(@Param('id', ParseIntPipe) id: number): Promise<void>{
        return this.markerService.deleteById(id);
    }
}
