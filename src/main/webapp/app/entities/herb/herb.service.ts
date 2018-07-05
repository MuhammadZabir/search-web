import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHerb } from 'app/shared/model/herb.model';

type EntityResponseType = HttpResponse<IHerb>;
type EntityArrayResponseType = HttpResponse<IHerb[]>;

@Injectable({ providedIn: 'root' })
export class HerbService {
    private resourceUrl = SERVER_API_URL + 'api/herbs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/herbs';

    constructor(private http: HttpClient) {}

    create(herb: IHerb): Observable<EntityResponseType> {
        return this.http.post<IHerb>(this.resourceUrl, herb, { observe: 'response' });
    }

    update(herb: IHerb): Observable<EntityResponseType> {
        return this.http.put<IHerb>(this.resourceUrl, herb, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHerb>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHerb[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHerb[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
