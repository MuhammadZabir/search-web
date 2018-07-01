import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { JhiLanguageHelper } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { SearchByImageService } from '../search-by-image/service/search-by-image.service';

@Component({
    selector: 'jhi-search-by-text',
    templateUrl: './search-by-text.component.html',
    styleUrls: ['../search-by-image/search-by-image.component.css']
})
export class SearchByTextComponent {
    image: any;
    result: any[];
    currentSearch: string;

    constructor(private languageHelper: JhiLanguageHelper, private searchByImageService: SearchByImageService) {}

    searchByText() {
        const formData = new FormData();

        if (this.currentSearch) {
            this.searchByImageService.searchByText(this.currentSearch).subscribe((response: HttpResponse<string[]>) => {
                const directories: string[] = response.body;
                this.result = [];
                for (let directory of directories) {
                    this.searchByImageService.getImage(directory).subscribe(result => {
                        this.result.push(result);
                    });
                }
            });
        }
    }

    createImageFromBlob(image: Blob) {
        let reader = new FileReader();
        reader.addEventListener(
            'load',
            () => {
                this.result.push(reader.result);
            },
            false
        );

        if (image) {
            reader.readAsDataURL(image);
        }
    }

    clear() {
        this.currentSearch = undefined;
    }
}