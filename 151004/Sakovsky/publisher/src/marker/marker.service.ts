import { CACHE_MANAGER } from "@nestjs/cache-manager";
import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Cache } from "cache-manager";
import { markerCacheKeys, TTL } from "src/utils/redis/globalRedis";
import { Repository } from "typeorm";
import { MarkerRequestToCreate } from "../dto/request/MarkerRequestToCreate";
import { MarkerRequestToUpdate } from "../dto/request/MarkerRequestToUpdate";
import { Marker } from "../entities/Marker";

@Injectable()
export class MarkerService {
    constructor(
        @InjectRepository(Marker) private markerRepositoty: Repository<Marker>,
        @Inject(CACHE_MANAGER) private cacheManager: Cache,
    ) {}

    getAll(): Promise<Marker[]> {
        return this.markerRepositoty.find();
    }

    async createMarker(marker: MarkerRequestToCreate): Promise<Marker> {
        const createdMarker = await this.markerRepositoty.save(marker);

        const allMarkers = await this.getAll();
        this.cacheManager.set(markerCacheKeys.markers, allMarkers, TTL);
        return createdMarker;
    }

    getById(id: number): Promise<Marker> {
        return this.markerRepositoty.findOneBy({ id });
    }

    async deleteById(id: number): Promise<void> {
        try {
            await this.markerRepositoty.findOneByOrFail({ id });

            let allMarkers = await this.cacheManager.get<Marker[]>(
                markerCacheKeys.markers,
            );
            allMarkers = allMarkers.filter((marker) => marker.id !== id);
            this.cacheManager.set(markerCacheKeys.markers, allMarkers, TTL);
            this.cacheManager.del(markerCacheKeys.markers + id);
        } catch (error) {
            throw new NotFoundException(`Marker with id: ${id} not found`);
        }
        await this.markerRepositoty.delete(id);
    }

    async updateMarker(markerDto: MarkerRequestToUpdate): Promise<Marker> {
        const marker = await this.markerRepositoty.findOneBy({
            id: markerDto.id,
        });
        if (!marker) {
            throw new NotFoundException(
                `Marker with id: ${markerDto.id} not found.`,
            );
        }
        marker.name = markerDto.name;

        let allMarkers = await this.cacheManager.get<Marker[]>(
            markerCacheKeys.markers,
        );
        allMarkers = allMarkers.map((marker) => {
            if (marker.id === markerDto.id) {
                marker.name = markerDto.name;
            }
            return marker;
        });
        this.cacheManager.set(markerCacheKeys.markers, allMarkers, TTL);
        this.cacheManager.set(markerCacheKeys.markers + marker.id, marker, TTL);

        return this.markerRepositoty.save(marker);
    }
}
