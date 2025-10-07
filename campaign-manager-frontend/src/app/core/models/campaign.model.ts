export type CampaignStatus = 'DRAFT' | 'ACTIVE' | 'PAUSED' | 'COMPLETED';

export interface Campaign {
  id: string;
  name: string;
  description: string | null;
  budget: number | null;
  startDate: string | null;
  endDate: string | null;
  targetAudience: string | null;
  status: CampaignStatus;
  createdAt: string;
  updatedAt: string;
  assetCount: number;
}

export interface CampaignRequest {
  name: string;
  description?: string;
  budget?: number;
  startDate?: string;
  endDate?: string;
  targetAudience?: string;
  status: CampaignStatus;
}

export interface CampaignAsset {
  id: string;
  fileName: string;
  s3Url: string;
  fileType: string;
  fileSize: number;
  uploadedAt: string;
}