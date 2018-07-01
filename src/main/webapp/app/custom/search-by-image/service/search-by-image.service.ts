import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ResponseWrapper } from '../../../shared/model/response-wrapper.model';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable()
export class SearchByImageService {
    private resourceSearchImageUrl = '/api/search/by-image';
    private resourceGetImageUrl = '/api/search/get-image';
    private resourceSearchTextUrl = '/api/search/by-text';
    constructor(private httpClient: HttpClient, private domSanitizer: DomSanitizer) {}

    searchByImage(formData: FormData) {
        return this.httpClient.post<string[]>(this.resourceSearchImageUrl, formData, { observe: 'response' });
    }

    searchByText(search) {
        return this.httpClient.get<string[]>(`${this.resourceSearchTextUrl}/${search}`, { observe: 'response' });
    }

    getImage(directory: string): Observable<any> {
        const formData = new FormData();
        formData.append('directory', directory);
        return this.httpClient
            .post(this.resourceGetImageUrl, formData, { responseType: 'blob' })
            .map(e => this.domSanitizer.bypassSecurityTrustUrl(URL.createObjectURL(e)));
    }
}
