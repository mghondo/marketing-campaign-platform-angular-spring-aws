import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Campaign, CampaignRequest, CampaignAsset } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {
  private readonly API_URL = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCampaigns(status?: string): Observable<Campaign[]> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<Campaign[]>(`${this.API_URL}/campaigns`, { params });
  }

  getCampaignById(id: string): Observable<Campaign> {
    return this.http.get<Campaign>(`${this.API_URL}/campaigns/${id}`);
  }

  createCampaign(data: CampaignRequest): Observable<Campaign> {
    return this.http.post<Campaign>(`${this.API_URL}/campaigns`, data);
  }

  updateCampaign(id: string, data: CampaignRequest): Observable<Campaign> {
    return this.http.put<Campaign>(`${this.API_URL}/campaigns/${id}`, data);
  }

  deleteCampaign(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/campaigns/${id}`);
  }

  uploadAsset(campaignId: string, file: File): Observable<CampaignAsset> {
    const formData = new FormData();
    formData.append('file', file);
    
    return this.http.post<CampaignAsset>(
      `${this.API_URL}/campaigns/${campaignId}/assets`,
      formData
    );
  }

  getAssets(campaignId: string): Observable<CampaignAsset[]> {
    return this.http.get<CampaignAsset[]>(`${this.API_URL}/campaigns/${campaignId}/assets`);
  }

  deleteAsset(assetId: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/assets/${assetId}`);
  }
}