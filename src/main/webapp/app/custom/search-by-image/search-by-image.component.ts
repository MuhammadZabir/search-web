import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Image } from './model/image.model';
import { JhiLanguageHelper } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { SearchByImageService } from './service/search-by-image.service';
import { ImageSearchDTO } from 'app/custom/search-by-image/model/imageSearchDTO.model';
import { Herb } from 'app/custom/search-by-image/model/herb.model';

@Component({
    selector: 'jhi-search-by-image',
    templateUrl: './search-by-image.component.html',
    styleUrls: ['./search-by-image.component.css']
})
export class SearchByImageComponent implements OnInit {
    imageToBeSearch: Image;
    droppable: boolean = false;
    messages: string;
    image: any;
    result: any[];
    herb: Herb;

    constructor(private languageHelper: JhiLanguageHelper, private searchByImageService: SearchByImageService) {}

    ngOnInit() {
        const xhr = new XMLHttpRequest();

        if (xhr.upload) {
            this.droppable = true;
        }
    }

    fileDragHover(event) {
        event.stopPropagation();
        event.preventDefault();
    }

    fileSelectHandler(event) {
        this.fileDragHover(event);

        const files = event.target.files || event.dataTransfer.files;
        this.imageToBeSearch = new Image();
        this.imageToBeSearch.file = files[0];
        this.imageToBeSearch.filename = files[0].name;

        for (let i = 0; i < files.length; i++) {
            this.parseFile(files[i]);
        }
    }

    parseFile(file) {
        const reader: FileReader = new FileReader();
        reader.onload = () => {
            this.image = reader.result;
        };
        reader.readAsDataURL(file);
        this.messages = 'File Information: ' + file.name;
    }

    searchByImage() {
        const formData = new FormData();

        if (this.imageToBeSearch) {
            formData.append('imageFile', this.imageToBeSearch.file);
            formData.append('filename', this.imageToBeSearch.filename);
            this.searchByImageService.searchByImage(formData).subscribe((response: HttpResponse<ImageSearchDTO>) => {
                const directories: string[] = response.body.directories;
                this.herb = response.body.herb;
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
}
