export type user = {
  username: string;
  email: string;
  role: string;
  createdAt: string;
};

export interface tag {
  id: number;
  name: string;
}

export type problemPreview = {
  id: number;
  title: string;
  body: string;
};

export type problem = {
  id: number;
  authorId: number;
  body: string;
  createdAt: string;
  memoryLimit: number;
  restrictions: string;
  timeLimit: number;
  title: string;
};
