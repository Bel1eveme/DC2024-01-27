import { Module } from "@nestjs/common";
import { AuthController } from "./auth.controller";
import { AuthService } from "./auth.service";
import { AuthorModule } from "src/author/author.module";
import { JwtModule } from "@nestjs/jwt";

@Module({
    imports: [
        AuthorModule,
        JwtModule.register({
            secret: 'DC_LAB6',
            signOptions: {
                expiresIn: "1d",
            },
        }),
    ],
    controllers: [AuthController],
    providers: [AuthService],
})
export class AuthModule {}
